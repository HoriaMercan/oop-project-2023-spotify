### Horia Mercan - Grupa 323CA

# Etapa 1

# Descirerea solutiei

In cadrul temei am incercat sa am un approach cat mai similar dezvoltarii unei aplicatii web
(o baza de date [package databases] de sine statatoare care contine informatiile necesare si API-uri [package gateways]
pentru accesul
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

# Etapa 2 - 11 decembrie 2023

Implementarile noilor comenzi s-au realizat prin completarea package-ului
[commands]. Un feature nou al aplicatiei a fost verificarea conexiunii user-ului la aplicatie,
asa ca am creat o noua interfata pentru astfel de actiuni [entities/requirements/RequireOnline].

Spre deosebire de etapa trecuta, tipul userilor a fost dezvoltat. Astfel, apar clase noi
precum [AbstractUser] (clasa care este supertipul oricarui alt tip de user).
Userii acum sunt de 3 tipuri: [User] (user normal), [Artist], [Host] (ultimii doi implementeaza particularitati
ai unui [ContentCreator]). Pentru implementarea propriu zisa a functionalitatilor ne folosim de interactiunea cu
clasa de tip Singleton [databases/MyDatabase] folosindu-ne de functii specifice Colectiilor si functii lambda.

Sistemul de paginare este construit in jurul unui page handler ([users/functionalities/PageHandler]) care se foloseste
de functii lambda pentru a returna metodele atribuite fiecarui tip de pagina (astfel, pentru orice entitate care poate
genera o pagina vom implementa interfata [pagesystem/Pageable]). Am adaugat metode noi in clasele
abstracte [entities/audioCollections/AudioCollection] si [entities/audioFiles/AudioFile] pentru a opera mai bine
colectiile de melodii/episoade de podcast si pentru reducerea codului.
Am incercat sa fragmenetez cat mai mult si mai util functiile de stergere a fiecarui tip de user
(verificand pentru fiecare tip particular de user daca fisierele lui sunt ascultate). Toate functiile acestea sunt
implementate
in [gateways/AdminAPI].

## Observatie

Vreau sa fac observatia ca pentru realizarea tipurilor de comenzi am fost inspirat de Factory Pattern,
chiar daca Input-ul/Output-ul comenzilor se afla in fiecare clasa a comenzii ca subtype.
Pentru etapa urmatoare vreau sa ma gandesc unde ar fi mai interesant sa folosesc Visitor Pattern sau Decorator Pattern.

# Etapa 3 - 12 ianuarie 2024

Implementarile noilor comenzi se gasesc in [commands/wrapped], [commands/monetization],
[commands/recommendation] si [commands/usersAdministration]. 

Pentru implementare m-am bazat pe 4 Design Pattern-uri:
* Singleton (pentru mentinerea unei clase tip baza de date)
* Visitor (pentru implementarea si extinderea unor functionalitati legate doar de anumite clase 
(more specific: Song, PodcastEpisode))
* Observer (pentru trimiterea notificatiilor cu ajutorul unui handler)
* Builder

Voi prezenta particularitati de implementare a fiecarei noi functionalitati
### Wrapped

In pachetul [entities/wrapper] gasim doua noi pachete [handlers] (care implementeaza formatul necesar la output)
si [statistics] (care implementeaza structura claselor care tin evidenta statisticilor). Inspiratia pentru aceasta
separare si modelul de implementare a fost Factory Method pattern. WrapperStatistics si subclasele prezinta metode pentru:

* getDataWrapping() -> construieste formatul necesar la output folosind o subclasa a AbstractDataWrapping
* addOneListen() -> adauga o noua ascultare la statisticile unui artist/host/user

Pentru eficienta, am implementat un builder pentru <User/Artist/Host>DataWrapping
si Visitor pattern folosind interfetele Listenable si VisitorWrapper.

Fiecare OneListen este trimis din UserPlayer-ul atribui fiecarui utilizator, prin adaugarea unui nou
FunctionalInterface pentru AudioFileSelector numit AudioFileSender
### Monetization

Implementarile pentru ArtistRevenue si UserPayment se gasesc in [entities/monetization] si plata este facuta atunci cand
incepe ad-ul, respectiv se incheie un abonament de tip premium (ori finalul programului).

# Notifications

Folosind Observer Pattern, am implementat Clasa NotificationHandler folosita de toti userii, iar functia de
notifySubscribers a fost integrata in interfata ContentCreator (intrucat doar artistii si moderatorii de podcast
au acest tip de functionalitate).

