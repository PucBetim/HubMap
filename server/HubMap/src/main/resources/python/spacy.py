import spacy

from spacy.tokenizer import Tokenizer
from spacy.lang.pt import Portuguese

from nltk.tokenize import word_tokenize

nlp = spacy.load("pt_core_news_lg")

def getBagOfWords(sentence):   
    
    cleanSentence = removeStopWords(sentence)
    
    doc = nlp(cleanSentence)
       
    bagOfWordsArray = list()
    
    for token in doc:
        bagOfWordsArray.append(token.lemma_.lower())
    
    return bagOfWordsArray

def removeStopWords(sentence):
    all_stopwords = nlp.Defaults.stop_words

    all_stopwords |= {".", ",", "+", "-", "/", "|",}
    
    token_sentence = word_tokenize(sentence, "portuguese")
    token_sentence_without_sw = [word for word in token_sentence if not word in all_stopwords and not word.isdigit()]
    
    strToReturn = ""
    
    for word in token_sentence_without_sw:
        strToReturn += word + " "
    
    return strToReturn

bagOfWords = getBagOfWords(sentence)