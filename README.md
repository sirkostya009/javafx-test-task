# JavaFX Test Task

## How to run:
1. Run in IntelliJ IDEA with at least Java SDK version 17.
2. Make sure you have `MYSQL_URL` JDBC connection URL defined on the path.
3. Run the `Main` classes
   - To run the JavaFX application, you will have to run `mvn javafx:run`, don't forget to `cd` into `javafx-ui` folder for that.
4. On startup, `cron-scraper` runs and fetches the data from the website and saves it to database.
