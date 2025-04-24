🎬 Leffakerho – Elokuvasovellus
-------------------------------
Leffakerho on Java- ja Vaadin-pohjainen SPA (Single Page Application), 
jonka avulla käyttäjät voivat selata, arvostella ja hallinnoida elokuvia, 
sekä rakentaa omaa katselulistaansa. Sovellus sisältää käyttäjähallinnan ja tietoturvaominaisuudet, 
toimii Docker-kontissa ja yhdistyy PostgreSQL-tietokantaan Docker Composen avulla.
Tietokantatyökalu postgreSQL ja kannan nimi "leffakerho".

---

1. Kun sovelluksen käynnistää se aukeaa selaimessa osoitteessa:
http://localhost:8080

---
2. Projektin rakenne
leffakerho/
├── src/
│   ├── main/
│   │   ├── java/com/example/application/
│   │   │   ├── config/           # Konfiguraatioluokat (DataLoader, WebConfig)
│   │   │   ├── data/             # Entiteetit ja repositoryt
│   │   │   ├── security/         # Spring Security -konfiguraatiot ja käyttäjänhallinta
│   │   │   ├── services/         # Sovelluslogiikka (Service-luokat)
│   │   │   ├── views/            # Vaadin-näkymät ja layoutit
│   │   │   │   ├── login/        # Kirjautumissivu
│   │   │   │   ├── mainview/     # MainView.java
│   │   │   │   ├── movies/       # MovieDetailView.java, MoviesView.java
│   │   │   │   ├── reviews/      # ReviewsView.java
│   │   │   │   ├── profile/      # ProfileView.java
│   │   │   │   ├── watchlist/    # WatchListView.java
│   │   │   │   ├── admin/        # AdminView.java
│   │   │   │   └── accessdenied/ # AccessDeniedView.java
│   │   │   └── application/      # Spring Boot -pääsovellusluokka
│   │   ├── frontend/
│   │   │   ├── themes/leffakerho/
│   │   │   │   ├── components/   # komponenttien tyylit
│   │   │   │   ├── views/        # Yksittäisten näkymien tyylit (.css) ns. custom tyylit
│   │   │   │   ├── theme.json    # Vaadin-teeman määritykset
│   │   │   │   └── styles.css    # Yleiset tyylit
│   ├── resources/
│   │   ├── images/               # Kuvia, kuten logo, accessdenied
│   │   ├── application.properties # Spring-sovelluksen asetukset
│   │   └── data.sql              # Esitäyttö tietokantaan
├── docker-compose.yml            # Docker Compose määrittely
├── Dockerfile                    # Docker-kuvan rakentaminen
├── pom.xml                       # Maven-projektin määrittely
└── .gitignore

---
3. Tietoturva ja käyttäjät

    - Käytössä on Spring Security.

    - Sovelluksessa on Admin ja User -roolit.

    - Näkymät jaetaan käyttäjäroolin mukaan:

	- Vaatii kirjautumisen login sivulta (on käyttäjät user ja admin, samoilla salasanoilla)

        - Kaikki kirjautuneet näkevät päänäkymän

        - User & Admin voivat käyttää sisältösivuja

        - Vain Admin näkee Admin-näkymän

    	- Estettyyn näkymään ohjataan käyttäjä, jolla ei ole oikeuksia (access-denied reitille)

---
4.Käyttöliittymä & Toiminnallisuudet
🏠 MainView (Kotisivu)

    	- Logo ja tervetuloteksti kirjautuneelle käyttäjälle

    	- Vasemmassa laidassa valikko:

        	- Koti, Elokuvat, Profiili, Arvostelut, Suosikit, Admin (vain adminille)

    	- Alareunassa kirjautunut käyttäjä + uloskirjautuminen

🎬 MoviesView (Elokuvat)

    	- Grid kaikista elokuvista

    	- 5 suodatinta: nimi, ohjaaja, genre, julkaisuvuosi, keskiarvosana

    	- Elokuvan klikkaus vie yksityiskohtaiselle näkymälle

    	- Admin voi poistaa elokuvan

📄 MovieDetailView

    	- Näyttää elokuvan tiedot

    	- Listaa kaikki arvostelut ja näyttää keskiarvon

👤 ProfileView

    	- Käyttäjä voi muokata omaa nimeä ja sähköpostia

    	- Käyttäjä voi lisätä uusia elokuvia

    	- Käyttäjä voi poistaa itse lisäämiään elokuvia

📝 ReviewsView

    	- Grid kaikista arvosteluista

    	- Suodatus esim. elokuvan mukaan

    	- Kaikki voivat lisätä arvosteluita

    	- Admin voi muokata ja poistaa arvosteluja

⭐ WatchListView (Suosikit)

    	- Lisää elokuvia suosikkeihin

    	- Merkkaa katselluksi

🔐 LoginView

    	- Kirjautumissivu Spring Securityn kautta

🛑 AccessDeniedView

    	- Käyttäjä ohjataan tänne, jos yrittää admin-sivulle ilman oikeuksia

🛠️ AdminView

    	- Vain admin näkee

    Admin voi:

        - Lisätä ja poistaa käyttäjiä

        - Poistaa elokuvia (MoviesView)

        - Muokata ja poistaa arvosteluita (ReviewsView)
---
5. Ulkoasu:

    	- SPA-sovellus Vaadinilla

     	- Header, Footer, Navigointi

    	- Erilaisia näkymia (login, main, elokuvat, yksittäinen elokuva, profiili, arvostelut, suosikit ja admin, sekä accessdenied näkymät)

    	- Lumo Utility -tyylejä ja omia CSS-määrittelyjä käytössä, sekä custom-tyylitiedostoja

---
6. Salasanat:

    	- Salasanat kryptattu BCryptillä

---
7. Tietokanta ja Docker
	
	- Sovellus käyttää PostgreSQL-tietokantaa. Kehitysympäristössä hyödynnetään data.sql-tiedostoa, joka sisältää valmiin testidatan: käyttäjät, roolit, elokuvat ja arvostelut.
		# PostgreSQL-asetukset (application.properties)
		spring.datasource.url=jdbc:postgresql://localhost:5432/leffakerho
		spring.datasource.username=postgres
		spring.datasource.password=root

		# Hibernate-asetus: skeema luodaan ja pudotetaan sovelluksen elinkaaren aikana (kehityskäyttöön)
		spring.jpa.hibernate.ddl-auto=create-drop

		# Suorita data.sql aina sovelluksen käynnistyessä
		spring.sql.init.mode=always
		spring.jpa.defer-datasource-initialization=true


		- Testidata: data.sql
			Tiedosto sisältää esimerkkikäyttäjät (user ja admin), heidän roolinsa, kattavan listan elokuvia sekä arvosteluita.
			Tämä mahdollistaa sovelluksen käytön heti ilman manuaalista datan lisäämistä:
				Kirjautuminen: Käyttäjä/Salasana
					            user / user
					            admin / admin
    			Esimerkkielokuvia eri genreissä    
			    Arvosteluita useilta käyttäjiltä
    			Suodatus- ja hallintatoiminnot heti testattavissa


    	- Dockerfile rakentaa JAR-tiedostosta konttikuvan

    	- docker-compose.yml käynnistää sovelluksen ja PostgreSQL-tietokannan

---
8. Käynnistys Dockerilla

1. **Rakenna projekti (jos et ole vielä buildannut JARia):**
   ```bash
   ./mvnw clean package

2. Käynnistä sovellus Dockerilla:
docker compose up --build

---
9. Lokalisointi

   Sovelluksessa on toteutettu lokalisointi (FI/EN) resurssitiedostojen avulla.
   MainView-sivu näyttää eri tekstit käyttäjän kieliasetuksen mukaan.


---
10. Tekniset yksityiskohdat

    	- Vaadin 24+

    	- Java 17 (Eclipse Temurin)

    	- PostgreSQL 15

    	- Spring Boot + Security + JPA
