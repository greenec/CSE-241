DROP TABLE supplier;
DROP TABLE supplierPhone;
DROP TABLE shipment;
DROP TABLE supplies;
DROP TABLE ships;
DROP TABLE receives;
DROP TABLE contains;
DROP TABLE lot;
DROP TABLE madeFrom;
DROP TABLE productLine;
DROP TABLE sells;

CREATE TABLE supplier (
    supplierId NUMBER PRIMARY KEY,
    supplierName VARCHAR(50) NOT NULL,
    streetNumber NUMBER,
    streetName VARCHAR(80),
    city VARCHAR(50),
    state VARCHAR(20),
    zipCode NUMBER
);

CREATE TABLE supplierPhone (
	supplierId NUMBER,
	phoneNumber VARCHAR(20)
);

CREATE TABLE shipment (
    shipmentId NUMBER PRIMARY KEY,
    unitPrice DECIMAL(10, 4) NOT NULL,
    quantity NUMBER,
    supplierId NUMBER
);

CREATE TABLE supplies (
	supplierId NUMBER,
	receiverId NUMBER,
	CONSTRAINT supplies_pk PRIMARY KEY (supplierId, receiverId)
);

CREATE TABLE ships (
	shipmentId NUMBER PRIMARY KEY,
	supplierId NUMBER
);

CREATE TABLE receives (
	shipmentId NUMBER PRIMARY KEY,
	supplierId NUMBER
);

CREATE TABLE contains (
	shipmentId NUMBER,
	lotId NUMBER,
	CONSTRAINT contains_pk PRIMARY KEY (shipmentId, lotId)
);

CREATE TABLE lot (
	lotId NUMBER PRIMARY KEY,
	supplierId NUMBER,
	productLineId NUMBER
);

CREATE TABLE madeFrom (
	productLineId NUMBER,
	lotId NUMBER,
	CONSTRAINT madeFrom_pk PRIMARY KEY (productLineId, lotId)
);

CREATE TABLE productLine (
	productLineId NUMBER PRIMARY KEY,
	productName VARCHAR(50),
	price DECIMAL (10, 4)
);

CREATE TABLE sells (
	supplierId NUMBER,
	productLineId NUMBER,
	salePrice DECIMAL (10, 4),
	CONSTRAINT sells_pk PRIMARY KEY (supplierId, productLineId)
);