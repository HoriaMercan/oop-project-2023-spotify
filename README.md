# Descirerea solutiei

In cadrul temei am incercat sa am un approach cat mai similar dezvoltarii unei aplicatii web
(o baza de date [package databases] de sine statatoare care contine informatiile necesare si API-uri [package gateways] pentru accesul
ei
din cadrul diverselor componente)

## Organizarea comenzilor

Clasa AbstractCommand aflata in package-ul commands
organizeaza trecerea datelor de la formatul de input la cel de output. Astfel, aici am integrat si cele clase interne
statice CommandInput si CommandOutput. Toate comenzile necesare functionalitatii aplicatiei mostenesc metodele clasei.
Folosind Jackson, am handle-uit intr-un mod easy-to-use si automat trecerea de la un JSON de input la organizarea logica
in clase.

## Entitati prezente in cadrul aplicatiei

Organizarea entitatilor este prezentata in package-ul [entities]. Cele mai interesante sunt clasele subpackage-ului
audioFileSelector si clasa User.

### audioFileSelector subpackage

Clasele au fost concepute astfel sa reprezinte 'bucati din puzzle' a ceea ce inseamna un iterator (vreau sa mentionez ca
inca nu discutasem la curs despre iteratorii formalizati in Java, poate o sa tin cont de asta la urmatoarea etapa). Am
inclus in pachet interfete pentru metode precum current, next, end, outOfBound pentru a le customiza de fiecare data
cand playerului i se schimbai repeat status-ul si shuffle status-ul. Customizarea m-a ajutat ca codul din clasa interna
UserPlayer a clasei User sa fie mult mai clean si general.

### User class

Fiecare user are in mod unic un player care poate fi considerat activ/inactiv. Astfel clasa User contine o clasa ampla
denumita UserPlayer care are rolurile:

* Tine minte parcursul podcasturilor ascultate
* Contorizeaza si e mereu updated in legatura cu ce melodie/podcast ruleaza la un anumit moment in player
* Desfasoara audio files in conformitate cu statusurile de shuffle si repeat folosind un obiect de tipul
  AudioFileSelector
* Ofera toate informatiile necesare despre activitatea playerului (tipul de fisiere ce sunt incarcate in player)

