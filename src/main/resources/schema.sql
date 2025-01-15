DROP TABLE if EXISTS PRICES;

CREATE TABLE PRICES (
    ID BIGINT AUTO_INCREMENT,
    BRAND_ID VARCHAR(20),
    START_DATE DATETIME,
    END_DATE DATETIME,
    PRICE_LIST INT,
    PRODUCT_ID VARCHAR(20),
    PRIORITY INT,
    PRICE DECIMAL(10, 2),
    CURRENCY VARCHAR(3)
);

CREATE INDEX idx_prices_product_date
ON PRICES (BRAND_ID, PRODUCT_ID, START_DATE, END_DATE);