DELETE FROM shipment WHERE shipmentId NOT IN (SELECT shipmentId FROM ships) OR shipmentId NOT IN (SELECT shipmentId FROM receives) OR shipmentId NOT IN(SELECT shipmentId FROM contains);

DELETE FROM ships WHERE shipmentId NOT IN (SELECT shipmentId FROM shipment);
DELETE FROM receives WHERE shipmentId NOT IN (SELECT shipmentId FROM shipment);

DELETE FROM contains WHERE shipmentId NOT IN (SELECT shipmentId FROM shipment);
DELETE FROM lot WHERE shipmentId NOT IN (SELECT shipmentId FROM contains);
DELETE FROM madeFrom WHERE lotId NOT IN (SELECT lotId FROM lot);