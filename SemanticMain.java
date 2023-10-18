import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

public class SemanticMain {
    public List<String> listVocabulary = new ArrayList<>();  //List that contains all the vocabularies loaded from the csv file.
    public List<double[]> listVectors = new ArrayList<>(); //Associated vectors from the csv file.
    public List<Glove> listGlove = new ArrayList<>();
    public final List<String> STOPWORDS;

    public SemanticMain() throws IOException {
        STOPWORDS = Toolkit.loadStopWords();
        Toolkit.loadStopWords();
    }


    public static void main(String[] args) throws IOException {
        StopWatch mySW = new StopWatch();
        mySW.start();
        SemanticMain mySM = new SemanticMain();
        mySM.listVocabulary = Toolkit.getListVocabulary();
        mySM.listVectors = Toolkit.getlistVectors();
        mySM.listGlove = mySM.CreateGloveList();

        List<CosSimilarityPair> listWN = mySM.WordsNearest("computer");
        Toolkit.PrintSemantic(listWN, 5);

        listWN = mySM.WordsNearest("phd");
        Toolkit.PrintSemantic(listWN, 5);

        List<CosSimilarityPair> listLA = mySM.LogicalAnalogies("china", "uk", "london", 5);
        Toolkit.PrintSemantic("china", "uk", "london", listLA);

        listLA = mySM.LogicalAnalogies("woman", "man", "king", 5);
        Toolkit.PrintSemantic("woman", "man", "king", listLA);

        listLA = mySM.LogicalAnalogies("banana", "apple", "red", 3);
        Toolkit.PrintSemantic("banana", "apple", "red", listLA);
        mySW.stop();

        if (mySW.getTime() > 2000)
            System.out.println("It takes too long to execute your code!\nIt should take less than 2 second to run.");
        else
            System.out.println("Well done!\nElapsed time in milliseconds: " + mySW.getTime());
    }

    public List<Glove> CreateGloveList() {
        List<Glove> listResult = new ArrayList<>();
        Glove obj = null;
        for (int i = 0; i < listVocabulary.size(); i++) {
            if (!STOPWORDS.contains(listVocabulary.get(i))) {
                obj = new Glove(listVocabulary.get(i), new Vector(listVectors.get(i)));
                listResult.add(obj);
                obj = null;
            }


        }


        //TODO Task 6.1
        return listResult;
    }

    public List<CosSimilarityPair> WordsNearest(String _word) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        for(int j = 0; j < listGlove.size(); j++){
            if(listGlove.get(j).getVocabulary().equals(_word)){
                _word = _word;
                break;
            }else if(j == listGlove.size()-1){
                _word = "error";
            }

        }for (int i = 0; i < listGlove.size(); i++) {
            if (listGlove.get(i).getVocabulary().equals(_word)) {
                Glove glo_obj;
                glo_obj = listGlove.get(i);
                for (Glove glove : listGlove) {
                    if (glove != glo_obj) {
                        listCosineSimilarity.add(new CosSimilarityPair(_word, glove.getVocabulary(), glo_obj.getVector().cosineSimilarity(glove.getVector())));
                    }
                }

            }
        }


        //TODO Task 6.2
        return  HeapSort.doHeapSort(listCosineSimilarity);
    }

    public List<CosSimilarityPair> WordsNearest(Vector _vector) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        boolean exist = false;

        for (int i = 0; i < listGlove.size(); i++) {
                if (listGlove.get(i).getVector().equals(_vector)) {
                    Glove glo_obj;
                    glo_obj = listGlove.get(i);
                    for (Glove glove : listGlove) {
                        if (glove != glo_obj) {
                            listCosineSimilarity.add(new CosSimilarityPair(glove.getVocabulary(), glove.getVocabulary(), _vector.cosineSimilarity(glove.getVector())));
                        }
                    }
                    exist = true;

                }


        }
        if(!exist){

            for (Glove glove : listGlove) {
                if(!glove.getVector().equals(_vector)){
                    listCosineSimilarity.add(new CosSimilarityPair(_vector, glove.getVocabulary(), _vector.cosineSimilarity(glove.getVector())));
                }

            }

        }

        //TODO Task 6.3
        return HeapSort.doHeapSort(listCosineSimilarity);
    }

    /**
     * Method to calculate the logical analogies by using references.
     * <p>
     * Example: uk is to london as china is to XXXX.
     * _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference. Moreover, "XXXX" is the vocabulary(ies) we'd like
     * to get from this method.
     * <p>
     * If _top <= 0, then returns an empty listResult.
     * If the vocabulary list does not include _secISRef or _firISRef or _firTORef, then returns an empty listResult.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _top      How many vocabularies to include.
     */
    public List<CosSimilarityPair> LogicalAnalogies(String _secISRef, String _firISRef, String _firTORef, int _top) {
        boolean included = true;
        Glove glo1= null;
        Glove glo2= null;
        Glove glo3= null;
        List<CosSimilarityPair> listResult = new ArrayList<>();
        if(_top <= 0){
            return listResult;
        }else if(!listVocabulary.contains(_firISRef) || !listVocabulary.contains(_firTORef) ||!listVocabulary.contains(_secISRef)){
            return listResult;
        }else{

            for (Glove glove : listGlove) {
                if (glove.getVocabulary().equals(_firISRef)) {
                    glo1 = glove;
                } else if (glove.getVocabulary().equals(_firTORef)) {
                    glo2 = glove;
                } else if (glove.getVocabulary().equals(_secISRef)) {
                    glo3 = glove;
                }
            }

        }



        Vector new_vector = glo3.getVector().subtraction(glo1.getVector()).add(glo2.getVector());
        listGlove.remove(glo1);
        listGlove.remove(glo2);
        listGlove.remove(glo3);
        for(int k = 0; k  <_top ; k++){
            listResult.add(WordsNearest(new_vector).get(k));
        }


        //TODO Task 6.4
        return listResult;
    }
}