(ns top.jmjoy.rbac.model
  (:require [clojure.java.jdbc :as jdbc]
            [top.jmjoy.rbac.config :as config]
            [top.jmjoy.rbac.util :as util]))

(defprotocol ORM
  (table-name [this]))

(defprotocol MapIntoRecord
  (map-> [this fields]))

(defrecord User [id name create-time]
  MapIntoRecord
  (map-> [this {:keys [id name create_time]}]
    (-> this
        (update :id id)
        (update :name name)
        (update :create-time create_time)))
  ORM
  (table-name [this] "user"))

(defn user-add [name]
  (let [create-time (util/current-timestamp)
        [{id :generated_key}] (jdbc/insert!
                               config/jdbc-config "user"
                               {"name" name
                                "create_time" create-time})]
    (User. id name create-time)))

(defrecord Role [id name create-time])

(defn role-add [name]
   (let [create-time (util/current-timestamp)
         [{id :generated_key}] (jdbc/insert!
                                config/jdbc-config "role"
                                {"name" name
                                 "create_time" create-time})]
     (Role. id name create-time)))

(defrecord Node [id name create-time parent-node])

(defn node-add
  ([name] (node-add name nil))
  ([name parent-node]
   (let [create-time (util/current-timestamp)
         parent-id (if parent-node (:id parent-node) 0)
         [{id :generated_key}] (jdbc/insert!
                                config/jdbc-config "node"
                                {"name" name
                                 "parent_id" parent-id
                                 "create_time" create-time})]
     (Node. id name create-time parent-node))))

(defn get-one-by-name [record-class name]
  (let [sql (format
             "select * from %s where name = '%s'"
             (.table-name record) name)]
    (if-let [result (first (jdbc/query config/jdbc-config sql))]
      (.map-> record result))))

