import spacy

from spacy.tokenizer import Tokenizer
from spacy.lang.pt import Portuguese

from nltk.tokenize import word_tokenize

nlp = spacy.load("pt_core_news_lg")

all_stopwords = nlp.Defaults.stop_words

all_stopwords |= {".", ",", "+", "-", "/", "|", 
              ":", "?", "...", "(", ")", "°",
              "_", "de", "em", "para", "com"
              "por", "%", "\"", "''", "'", "´", 
              "`", "~", "^", ";", "<", ">", "§",
              "=", "¨", "#", "!", "*"}

def getBagOfWords(sentence):   
    
    cleanSentence = removeStopWords(sentence)
    
    doc = nlp(cleanSentence)
       
    bagOfWordsArray = list()
    
    for token in doc:
        bagOfWordsArray.append(token.lemma_.lower())
    
    bagOfWordsArray = removeStopWordsFromArray(bagOfWordsArray)
    return bagOfWordsArray

def removeStopWords(sentence):
    
    token_sentence = word_tokenize(sentence, "portuguese")
    token_sentence_without_sw = [word for word in token_sentence if not word in all_stopwords and not word.isdigit()]
    
    strToReturn = ""
    
    for word in token_sentence_without_sw:
        strToReturn += word + " "
    
    return strToReturn

def removeStopWordsFromArray(bagOfWordsArray):
    
    tokens_without_sw = [word for word in bagOfWordsArray if not word in all_stopwords and not word.isdigit()]
    
    return tokens_without_sw

bagOfWords = getBagOfWords(sentence)