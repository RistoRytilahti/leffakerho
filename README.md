# 🎬 Leffakerho

Leffakerho on Java- ja Vaadin-pohjainen SPA (Single Page Application), jonka avulla käyttäjät voivat selata, arvostella ja hallinnoida elokuvia, sekä rakentaa omaa katselulistaansa. Sovellus sisältää käyttäjähallinnan ja tietoturvaominaisuudet, toimii Docker-kontissa ja yhdistyy PostgreSQL-tietokantaan Docker Composen avulla.

---

## 🚀 Sovelluksen käynnistys

**Sovellus aukeaa selaimessa osoitteessa:**
   [http://localhost:8080](http://localhost:8080)

---

## 🌐 Lokalisointi

Sovelluksessa on toteutettu lokalisointi (FI/EN) resurssitiedostojen avulla. MainView-sivu näyttää eri tekstit käyttäjän kieliasetuksen mukaan.

## 📦 Tietokanta ja testidata

- Sovellus käyttää PostgreSQL-tietokantaa. Tietokannan nimi "leffakerho". Kehitysympäristössä hyödynnetään `data.sql`-tiedostoa, joka sisältää valmiin testidatan: käyttäjät, roolit, elokuvat ja arvostelut.
- Esimerkkitunnukset:
  - Käyttäjä: `user / user`
  - Admin: `admin / admin`

**PostgreSQL-asetukset:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/leffakerho
spring.datasource.username=postgres
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```

## 🔎 Elokuvien suodatus

Käyttäjät voivat suodattaa elokuvia monipuolisesti:
- Nimen, ohjaajan, genren ja julkaisuvuoden perusteella
- Keskiarvosanan mukaan (suodatus Review-entiteetin perusteella)
- Useita ehtoja voi käyttää samanaikaisesti

---

## 🛠️ Sovelluksen käyttö ja näkymät

- **MainView**: Päänäkymä, jossa käyttäjä näkee valikon ja toivotustekstin
- **MoviesView**: Elokuvalistaus ja suodatus
- **MovieDetailView**: Yksittäisen elokuvan tiedot ja arvostelut
- **ProfileView**: Käyttäjän omien tietojen ja elokuvien hallinta
- **ReviewsView**: Kaikki arvostelut suodatettavissa
- **WatchListView**: Suosikkielokuvien hallinta
- **AdminView**: Käyttäjien ja sisältöjen hallinta (vain adminille)
- **LoginView**: Kirjautumissivu
- **AccessDeniedView**: Näkyy, jos käyttäjällä ei ole oikeuksia

---

## 🔐 Käyttäjät ja tietoturva

- Spring Security toteuttaa autentikoinnin ja roolituksen (User, Admin)
- Näkymät ja reitit suojattu käyttäjäroolin mukaan
- BCrypt-hashaus salasanan tallennukseen

---

## 🧩 Teknologiat ja rakenteet

- **Java 17**, **Spring Boot**, **Vaadin 24+**, **PostgreSQL 15**
- Frontendissä omat CSS-tyylit ja Lumo utility-luokat
- Maven-projekti, Dockerfile ja docker-compose.yml mukana

---


## 📦 Docker-rakenne (manuaalisesti)

1. **Rakenna projekti (jos et ole vielä buildannut JARia):**
   ```bash
   ./mvnw clean package
   ```

2. **Käynnistä sovellus Dockerilla:**
   ```bash
   docker compose up --build
   ```


Voit myös rakentaa ja ajaa sovelluksen Dockerilla manuaalisesti:

```bash
mvn clean package -Pproduction
docker build . -t leffakerho:latest
docker run -p 8080:8080 leffakerho:latest
```

---

## 🔗 Linkkejä

- [Vaadin-dokumentaatio](https://vaadin.com/docs)
- [Vaadin komponentit](https://vaadin.com/docs/latest/components)
- [Vaadin start](https://start.vaadin.com/)
- [Vaadin-esimerkit](https://vaadin.com/examples-and-demos)
- [Vaadin-foorumi](https://vaadin.com/forum)
- [Stack Overflow: vaadin](https://stackoverflow.com/questions/tagged/vaadin)

