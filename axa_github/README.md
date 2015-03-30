@author : Florian Lestic
@date : 29/03/2015
@description : Simple Web service Axa Text + Json

hote: https://axa-etradeweb.rhcloud.com/

URI :

Service des messages :
GET/POST : pattern: /service
@param : message
@description : Ajoute un message (un message comprend 1 ou plusieurs mots-clés séparés par des espaces)
@exemple : https://axa-etradeweb.rhcloud.com/service?message=monmotcle

Service top ip :
GET : pattern: /service_$type/top_ip
$type = text ou json : Résultat en json ou en texte
@param : limit
@description : Récupère les X ip qui ont envoyé le plus de message
@exemple : https://axa-etradeweb.rhcloud.com/service_json/top_ip?limit=3

Service top mots cles :
GET : pattern: /service_$type/top_mots
$type = text ou json : Résultat en json ou en texte
@param : limit
@param : order (2 => DESC sinon ASC) : optionnel
@description : Récupère les X ip qui ont envoyé le plus de message
@exemple : https://axa-etradeweb.rhcloud.com/service_text/top_mots?limit=8

Connexion mysql :
Root User: admin6PXVX59
Root Password: jN1EFg13rMsf
hote : axa-etradeweb.rhcloud.com:3306
https://axa-etradeweb.rhcloud.com/phpmyadmin/