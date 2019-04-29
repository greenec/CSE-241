DROP TABLE manufactures;
DROP TABLE ingredients;
DROP TABLE offers;
DROP TABLE madeFrom;
DROP TABLE contains;
DROP TABLE receives;
DROP TABLE ships;
DROP TABLE shipment;
DROP TABLE supplierPhone;
DROP TABLE supplies;
DROP TABLE batch;
DROP TABLE supplier;
DROP TABLE productLine;

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
	phoneNumber VARCHAR(20),
	
	FOREIGN KEY (supplierId)
        REFERENCES supplier(supplierId)
        ON DELETE CASCADE
);

CREATE TABLE productLine (
	productLineId NUMBER PRIMARY KEY,
	productName VARCHAR(50),
	price DECIMAL (10, 4)
);

CREATE TABLE ingredients (
	productId NUMBER,
	componentId NUMBER,
	
	FOREIGN KEY (productId)
		REFERENCES productLine(productId)
		ON DELETE CASCADE,
		
	FOREIGN KEY (componentId)
		REFERENCES productLine(productId)
		ON DELETE CASCADE
);

CREATE TABLE batch (
	batchId NUMBER PRIMARY KEY,
	supplierId NUMBER,
	processingDate DATE,
	
	FOREIGN KEY (supplierId)
		REFERENCES supplier(supplierId)
		ON DELETE SET NULL
);

CREATE TABLE shipment (
    shipmentId NUMBER PRIMARY KEY,
    unitPrice DECIMAL(10, 4) NOT NULL,
    quantity NUMBER,
    supplierId NUMBER,
    shipmentDate DATE,
	
	FOREIGN KEY (supplierId)
        REFERENCES supplier(supplierId)
        ON DELETE SET NULL
);

CREATE TABLE supplies (
	supplierId NUMBER,
	receiverId NUMBER,
	
	CONSTRAINT supplies_pk PRIMARY KEY (supplierId, receiverId),
	
	FOREIGN KEY (supplierId)
        REFERENCES supplier(supplierId)
        ON DELETE CASCADE,
		
	FOREIGN KEY (receiverId)
        REFERENCES supplier(supplierId)
        ON DELETE CASCADE
);

CREATE TABLE ships (
	shipmentId NUMBER PRIMARY KEY,
	supplierId NUMBER,
	
	FOREIGN KEY (shipmentId)
        REFERENCES shipment(shipmentId)
        ON DELETE CASCADE,
		
	FOREIGN KEY (supplierId)
        REFERENCES supplier(supplierId)
        ON DELETE SET NULL
);

CREATE TABLE receives (
	shipmentId NUMBER PRIMARY KEY,
	supplierId NUMBER,
	quantity NUMBER,
	
	FOREIGN KEY (shipmentId)
        REFERENCES shipment(shipmentId)
        ON DELETE CASCADE,
		
	FOREIGN KEY (supplierId)
        REFERENCES supplier(supplierId)
        ON DELETE SET NULL
);

CREATE TABLE contains (
	shipmentId NUMBER,
	lotId NUMBER,
	
	CONSTRAINT contains_pk PRIMARY KEY (shipmentId, lotId),

	FOREIGN KEY (lotId)
        REFERENCES lot(lotId)
        ON DELETE CASCADE,
		
	FOREIGN KEY (shipmentId)
        REFERENCES shipment(shipmentId)
        ON DELETE CASCADE
);

CREATE TABLE madeFrom (
	productLineId NUMBER,
	batchId NUMBER,
	
	CONSTRAINT madeFrom_pk PRIMARY KEY (productLineId, lotId),
	
	FOREIGN KEY (productLineId)
        REFERENCES productLine(productLineId)
        ON DELETE SET NULL,
		
	FOREIGN KEY (batchId)
        REFERENCES batch(batchId)
        ON DELETE CASCADE
);

CREATE TABLE manufactures (
	batchId NUMBER,
	supplierId NUMBER,
	
	CONSTRAINT manufactures_pk PRIMARY KEY (batchId, supplierId),
	
	FOREIGN KEY (batchId)
		REFERENCES batch(batchId)
		ON DELETE SET NULL,
	
	FOREIGN KEY (supplierId)
		REFERENCES supplier(supplierId)
		ON DELETE SET NULL
);

CREATE TABLE offers (
	supplierId NUMBER,
	productLineId NUMBER,
	salePrice DECIMAL (10, 4),
	
	CONSTRAINT sells_pk PRIMARY KEY (supplierId, productLineId),

	FOREIGN KEY (supplierId)
        REFERENCES supplier(supplierId)
        ON DELETE CASCADE,
	
	FOREIGN KEY (productLineId)
        REFERENCES productLine(productLineId)
        ON DELETE CASCADE
);