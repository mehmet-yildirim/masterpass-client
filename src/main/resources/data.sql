CREATE TABLE CONFIG
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    clientId   VARCHAR(50),
    merchantId VARCHAR(50),
    url        VARCHAR(50),
    encKey     VARCHAR(250),
    macKey     VARCHAR(250)
);

--INSERT INTO CONFIG (id, encKey, macKey) VALUES ( 1, 'C18BBD6742AAE6C398E80AC98CFD9CAA', '751FA1CA60C4BF5DA8BF408F1A366FB2' )