# 🌦️ Weather API Assessment

The framework for weather api automation testing is built with **Java 21**, **Serenity BDD** and **RestAssured**.It focuses on data-driven validations and automated reporting.

---

## 📊 Live Test Report
> [!IMPORTANT]
> **[👉 View the Latest Live Serenity Report](https://roshini98.github.io)**  
> *This report is automatically updated via GitHub Actions after every test execution.*

---

## 💡 Technical Approach
I designed this framework such that it focuses on code readability and maintainability:
- **Reporting:** I used `Serenity.recordReportData()` to highlight the key findings (like the warmest city or coldest state) directly in the HTML report, not just hidden in logs.
- **Data Handling:** I used Java Streams to process API responses, which allowed me to calculate things quickly like maximum and minimum temperatures with very clean and readable code.
- **Data-Driven Testing (CSV):** To find the coldest US state weather with a metadata input file, I integrated a  **CSV Reader** to parse the CSV file. This helped me to separate the test data from my test logic to maintain a clean code.
- **Action Pattern:** To ensure a clean code, I separated API call from the Step Definition files, so that the code is easily readable.

## 🛡️ Security & CI/CD
To ensure the security throughout the code. I have followed the below steps:
- **API Key Management:** The Weatherbit API key is not hardcoded and is injected at runtime via Maven properties.
- **GitHub Secrets:** In the CI/CD pipeline, the key is stored as an encrypted secret, ensuring it is never exposed in the repository or the build logs.

## 🏃 Local Execution
To run the suite locally, ensure you have **Maven** and **JDK 21** installed, then execute:

```bash
mvn clean verify -Dweather.api.key=YOUR_API_KEY_HERE