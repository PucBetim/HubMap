import spacy

from spacy.tokenizer import Tokenizer
from spacy.lang.pt import Portuguese

from nltk.tokenize import word_tokenize

nlp = spacy.load("pt_core_news_lg")

def getBagOfWords(sentence):   
    
    cleanSentence = removeStopWords(sentence)
    
    doc = nlp(cleanSentence)
       
    bagOfWordsArray = list()
    str = ""
    
    for token in doc:
        bagOfWordsArray.append(token.lemma_.lower())
        str += token.lemma_.lower() + ", "
        
    f = open("D:/tmp/python_out.txt", "a")
    f.write(str)
    f.close()
    return [bagOfWordsArray, str]

def removeStopWords(sentence):
    all_stopwords = nlp.Defaults.stop_words

    all_stopwords |= {".", ",", "+", "-", "/", "|", ":"}
    
    token_sentence = word_tokenize(sentence, "portuguese")
    token_sentence_without_sw = [word for word in token_sentence if not word in all_stopwords and not word.isdigit()]
    
    strToReturn = ""
    
    for word in token_sentence_without_sw:
        strToReturn += word + " "
    
    return strToReturn


result = getBagOfWords(sentence)
bagOfWords = result[0]
bagOfWordsString = result[1]
