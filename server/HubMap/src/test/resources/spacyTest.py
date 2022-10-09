import spacy
import nltk

from spacy.tokenizer import Tokenizer
from spacy.lang.pt import Portuguese

from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize

nltk.download('stopwords')
nltk.download('punkt')
nlp = spacy.load("pt_core_news_lg")

def getBagOfWords(sentence):   
    #sentence = "Com os nossos métodos conseguimos ensinar 100%, sobretudo alunos iniciantes, por meio de textos práticos, que favorecem a boa leitura e consequente compreensão do que é ensinado."
    
    f = open("D:/Workspace/TCC/temp.txt", "a")
    f.write(sentence)
    f.close()
    
    cleanSentence = removeStopWords(sentence)
    
    doc = nlp(cleanSentence)
       
    bagOfWordsArray = list()
    
    for token in doc:
        bagOfWordsArray.append(token.lemma_)
    
    return bagOfWordsArray

def removeStopWords(sentence):
    all_stopwords = nlp.Defaults.stop_words

    for word in stopwords.words("portuguese"):
        if not all_stopwords.__contains__(word):
            all_stopwords.add(word)
    all_stopwords |= {".", ",", "+", "-", "/",}
    
    token_sentence = word_tokenize(sentence, "portuguese")
    token_sentence_without_sw = [word for word in token_sentence if not word in all_stopwords]
    
    strToReturn = ""
    
    for word in token_sentence_without_sw:
        strToReturn += word + " "
    
    return strToReturn

bagOfWords = getBagOfWords(sentence)