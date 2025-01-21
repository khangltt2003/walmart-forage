import sqlite3
import csv

from numpy.lib.function_base import insert


class Populater:
    def __init__(self):
        self.db  = sqlite3.connect("./shipment_database.db")
        self.cursor = self.db.cursor()

    def populate(self):

        with open("./data/shipping_data_0.csv") as file_0:
            data_0 = csv.reader(file_0)
            self.populate_data_0(data_0)
        with open("./data/shipping_data_1.csv") as file_1:
            with open("./data/shipping_data_2.csv") as file_2:
                data_1 = csv.reader(file_1)
                data_2 = csv.reader(file_2)
                self.populate_data_1_and_data_2(data_1, data_2)
        self.db.commit()

    def populate_data_0(self, data_0):
        for i, row in enumerate(data_0):
            if i > 0:
                self.insert_product(row[2])
                self.insert_shipment(row[0], row[1],  row[2] , row[4])

    def populate_data_1_and_data_2(self, data_1, data_2):
        shipments = {}
        # {"shipment_id" : [origin, destination, {product: count}]}
        for i, row in enumerate(data_2):
            if i > 0:
                shipments[row[0]] = (row[1], row[2], {})

        for i, row in enumerate(data_1):
            if i > 0:
                shipments[row[0]][2][row[1]]  =  shipments[row[0]][2].get(row[1], 0) + 1;

        for shipment in shipments.values():
            for product, count in shipment[2].items():
                self.insert_product(product)
                self.insert_shipment(shipment[0], shipment[1], product, count)


    def insert_product(self, product):
        query = """
            insert or ignore into  product (name) values (?);
        """
        self.cursor.execute(query, (product,))


    def insert_shipment(self, origin, des,  product, quantity):
        query = "select id from product where name = ?;"
        self.cursor.execute(query, (product, ))
        product_id = self.cursor.fetchone()[0]

        query = """
            insert into shipment (product_id, quantity, origin, destination)
            values (?,?,?,?);
        """
        self.cursor.execute(query, (product_id, quantity, origin, des))

    def close(self):
        self.db.close()

if __name__  == "__main__":
    populater = Populater()
    populater.populate()
    populater.close()