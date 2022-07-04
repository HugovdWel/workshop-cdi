# Introductie
Deze workshop is deel van de DEA Course aan de Hogeschool Arnhem/Nijmegen. 
Onderwerp is het bekend raken met CDI en het unittesten van een JavaEE applicatie.

# Oefening
Deze oefening is het vervolg op de oefening [Simple the Rest](https://github.com/HANICA-DEA/workshop-simply-the-rest). 
De startcode van deze oefening is de eindcode van [Simple the Rest](https://github.com/HANICA-DEA/workshop-simply-the-rest).

## 1: Unittests toevoegen
We zullen beginnen met het toevoegen van unittests voor de klassen `HealthCheckResource` en `ItemResource`

### 1.1: `HealthCheckResource`
Voeg aan je project een dependency op jUnit toe en schrijf een unittest voor de methode uit de klasse ``HealthCheckResource``.

### 1.2: `ItemResource`
Schrijf voor iedere methode uit de klasse `ItemResource` een zinnige unittest. Omdat deze methode gebruik maakt van de 
`ItemService` en daar ook hard aan is gekopppeld, zal alleen de happy-flow getest kunnen worden. Voor nu is dat voldoende.

Waarschijnlijk zul je nog wel tegen een probleem aanlopen bij het testen van de methodes die een `Response` teruggeven.
Lees de stacktrace goed door en gebruik Google om een oplossing te vinden. Om je alvast in de goede richting te helpen: 
een implementatie `Response` wordt door je JavaEE Container aangeleverd. In de context van een 
unittest, maak je geen gebruik van je JavaEE Container. Je zult dus een bijbehorende `Exceptie` kunnen
verwachten, welke je kan terugvinden in de stacktrace. 

## 2: Toevoegen Dependency Injection
In het vorige onderdeel heb je unittests geschreven en mogelijk liep je tegen het probleem aan dat je niet goed wist
wat je nu eigenlijk moest testen. Waarschijnlijk kwam je niet veel verder dan het testen of het `Response` de te verwachtte
status-code bevatte. Maar of ook daadwerkelijk de juiste methodes op de `ItemService`, met de juiste parameters werden aangeroepen, 
heb je vast niet getest.

In de huidige vorm is dat ook uitzonderlijk lastig te testen. Dit komt omdat de `ItemService` hard gekoppeld is
aan de `ItemResource`. Wanneer je nu de `ItemResource` test, test je eigenlijk ook de `ItemService`. Buiten het probleem dat je
nu niet eens kan testen of de `ItemResource` correct gebruik maakt van de `ItemService`, heb je daarmee ook het probleem
dat je, wanneer een test faalt, niet goed weet of de fout nu in `ItemResource` of in de `ItemService` zit.

We gaan dit probleem oplossen door de `ItemService` los te koppelen van de `ItemResource`, met behulp van Dependency Injection.
Hiermee kunnen we de code beter laten voldoen aan de **D** van **SOLID**, het **D**pendency Inversion Principle. In het volgende
onderdeel kunnen we vervolgens laten zien hoe we nu wel enkel de `ItemResource` kunnen testen.

### 2.1: Het omkeren van de afhankelijkheid (Dependency Inversion)
Momenteel is de `ItemResource` afhankelijk van de `ItemService`. Stel dat de methode `getAll()` uit de `ItemService` van
naam zal veranderen, dan zal ook de `ItemResource` aangepast moeten worden. Dit gaan we aanpassen; we gaan 
eerst een interface maken die vast zal leggen welke methodes de `ItemResource` zal verwachten. Het type van de 
instantievariabele van de `ItemService` in de `ItemResource` wordt daarna aangepast naar de Interface. En Tot slot
moet de `ItemService` de interface implementeren.
* Hernoem de `ItemService` naar `HardCodedItemService`. De waarden zijn tenslotte hard-coded en het geeft dus beter
aan wat we van de klasse kunnen verwachten.
* Creëer een interface op basis van de `HardCodedItemService` en noem deze `ItemService`. Gebruik bij voorkeur de
refactor-functies van je IDE.
* Zorg dat de `HardCodedItemService` de interface `ItemService` implementeerd.
* Pas het type van de instantievariabele op de `ItemResource` aan.

### 2.2: Toepassen Dependency Injection
In de constructor van de `ItemResource` wordt een nieuwe instantie van de `HardCodedItemService` geïnstantieerd
en aan een instatievariabele toegewezen. Deze verantwoordelijkheid gaan we overdragen aan de JavaEE container, door 
gebruik te maken van CDI.
* Verwijder de volledige constructor van de `ItemResource`.
* Creëer een setter voor de instantievariabele genaamd `itemService`.
* Annoteer de setter met `@Inject`
* Om CDI 'aan' te zetten is het nog nodig om een *beans.xml* bestand op de juiste plek te zetten. Lees dit 
artikel voor meer informatie hierover: [An Introduction to CDI ](https://www.baeldung.com/java-ee-cdi)

## 3: Repareren van de unittests
Door het toevoegen van Dependency Injection zullen de unittests nu `NullPointerExceptions` gaan opleveren. 
In dit onderdeel gaan we dat weer repareren, maar daarbij gaan we ook gebruik maken van het gegeven dat de `ItemResource`
niet langer hard gekoppeld is aan de `ItemService`. En om de tests nog zinniger te maken introduceren we
het concept van [Mocking](https://medium.com/@piraveenaparalogarajah/what-is-mocking-in-testing-d4b0f2dbe20a), 
waarvoor we gebruik gaan maken van het framework [Mockito](https://site.mockito.org/)

### 3.1: Mocken van een `ItemService`
In iedere unittest zal nu eerst niet alleen een instantie gemaakt moeten worden van de SUT, maar ook van
een `ItemService`, die vervolgens via een *setter* op de SUT geplaatst moet worden. Wanneer we hier
een instantie voor maken van een `ItemService`, bijvoorbeeld de `HardCodedItemService`, dan blijft onze
unittest afhankelijk van die `HardCodedItemService`. Wanneer een test faalt, dan kan dit nog steeds komen
doordat er een bug zit in de `ItemResource` of de `HardCodedItemService`. Een zeer onwenselijke situatie, 
die we gaan oplossen door geen *echte* `ItemResource` te gebruiken, maar een gemockte.
* Voeg een dependency toe op de laatste versie van [Mockito](https://site.mockito.org/) (kies voor het artifactId: *mockito-core*)
* Voeg aan je testklasse de volgende instantie variabele toe:
```        
    private ItemService itemService;
```
* Gebruik de `setup()` methode om een gemockte `ItemService` aan je SUT toe te voegen:
```
    @BeforeEach
    void setup() {
        this.sut = new ItemResource();
        
        // Gebruik Mockito om een instantie te maken
        this.itemService = Mockito.mock(ItemService.class);
        
        // Gebruik de setter om de ItemService te zetten
        this.sut.setItemService(itemService);
    }
```

Run je tests. Mogelijk zijn er al test die nu slagen. Als dat zo is, dan toont dit
voornamelijk aan dat de unittests slecht zijn en weinig waarde toevoegen.
De methodes die op je gemockte `ItemService` worden aangeroepen bevatten namelijk nog geen
gedrag.

### 3.2 Toevoegen van zinnige unittests voor `getJsonItems()`
Nu we via Mockito een gemockte `ItemService` hebben gemaakt, kunnen we al het gewenste gedrag
van de `ItemResource` gaan testen. We gaan dit doen voor de tests van de methode: `getJsonItems()`.

Hiervoor gaan we de volgende tests schrijven, die al he gewenste gedrag van de methode vastleggen:
1. Wanneer de methode `getJsonItems()` wordt aangeroepen, moet op de `ItemService` de methode `getAll()` worden aangeroepen.
2. Wanneer de methode `getJsonItems()` wordt aangeroepen **en** de methode `getAll()` op de `ItemService` retourneert een Object, dan:
    * Is de status code van de `Response` 200
    * Levert de `getEnitity()` van de `Response` het Object terug dat de `getAll()` heeft geretourneerd

Haal hiervoor de bestaande unittest voor de betreffende methode weg. We zullen deze vervangen door twee nieuwe unittests.

* Schrijf een nieuwe unittest genaamd `getJsonItemsCallsGetAll`
* In de *Arrange* hoeft niks te gebeuren.
* In de *Act* moet op de SUT de betreffende methode worden aangeroepen. 
* In de *Assert* moet je testen of de methode `getAll()` ook daadwerkelijk is aangeroepen: 
```
    Mockito.verify(itemService).getAll(); 
```
* Schrijf een nieuwe uniitest genaamd `getJsonReturnsObjectFromServiceAsEntity()`
* In de *Arrange* moet je met Mockito zorgen dat je gemockte `ItemService` een specifiek Object retourneert:
```
    var itemsToReturn = new ArrayList<ItemDTO>();
    Mockito.when(itemService.getAll()).thenReturn(itemsToReturn);
```
* In de *Act* moet op de SUT de betreffende methode worden aangeroepen. 
* In de *Assert* moet je testen of de Entity in de `Response` hetzelfde Object is als dat je vanuit je mock hebt 
teruggegeven

### 3.2 Toevoegen van zinnige unittests voor de overige methodes
Maak nu ook zinnige unittests voor de overige methodes. Deze zullen vergelijkbaar zijn met de
hierboven beschreven tests.

### 3.3 Toevoegen unittests voor de foutafhandeling
Waarschijnlijk heb je nu alleen de happy-flow getest. Door het gebruik van de ExceptionMappers 
is het ook van belang dat de `ItemResource` de gegooide exepties van het type `IdAlreadyInUseException`
en `ItemNotAvailableException` niet vangt, maar gewoon doorgooit.

* In de *Arrange* zorg je ervoor dat wanneer de betreffende methode op de mock wordt aangeroepen, 
de exceptie van het juiste type wordt gegooit. Gebruik eventueel deze [tutorial](https://www.baeldung.com/mockito-exceptions).
* De *Act* en de *Assert* zullen enigzins samenvallen door de API van jUnit. In het *Assert* deel
test je of de verwachtte exceptie ook daadwerkelijk wordt gegooit. Gebruik eventueel deze [tutorial](https://howtodoinjava.com/junit5/expected-exception-example/).

## 4: Injecteren van een alternatieve `ItemService`
