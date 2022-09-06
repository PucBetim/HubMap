import spacy

nlp = spacy.load("pt_core_news_lg")

doc = nlp(sentence)

tokens = list()

for token in doc:
    tokens.append(token.text)