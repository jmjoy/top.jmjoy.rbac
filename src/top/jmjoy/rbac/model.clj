(ns top.jmjoy.rbac.model
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [schema.core :as s]
            [top.jmjoy.rbac.db :as db]
            [top.jmjoy.rbac.util :as util]
            [clojure.tools.logging :as log]))

(defmulti table-name
  "Get table name from record class."
  #(if (class? %) % (type %)))

(defmulti from-map
  "Construct a record from a map.
  First argument is record type and second is a map."
  (fn [cls result] cls))

(defmulti insert-record
  "Convert a record to database insert map."
  type)

(defn insert [record]
  "insert a record to database and return id."
  (let [[{id :generated_key}]
        (db/insert! (table-name record)
                    (insert-record record))]
    id))

(defn get-one-by-name
  "Get a record by `name` from database.
  Notice that record and table must has `name` field"
  [cls name]
  (log/info cls)
  ;; validate
  (s/validate s/Str name)
  ;; get from db
  (let [sql [(format "select * from %s where name = ?"
                     (table-name cls)),
             name]]
    (if-let [result (db/query-one sql)]
      (from-map cls result))))

;;==================================================

(defrecord User [id name create-time])

(defmethod table-name User [_] "user")

(defmethod from-map User [_ result]
  (let [{:keys [id name create_time]} result]
    (->User id name create_time)))

(defmethod insert-record User [record]
  {"name" (:name record)
   "create_time" (or (:create-time record) (util/current-timestamp))})

;;==================================================

(defrecord Role [id name create-time])

(defmethod table-name Role [_] "role")

(defmethod from-map Role [_ result]
  (let [{:keys [id name create_time]} result]
    (->Role id name create_time)))

(defmethod insert-record Role [record]
  {"name" (:name record)
   "create_time" (or (:create-time record) (util/current-timestamp))})

;;==================================================

(defrecord Node [id name create-time parent-node])

(defmethod table-name Node [_] "node")

(defmethod from-map Node [_ result]
  (let [{:keys [id name create_time parent_id]} result]
    (->Node id name create_time parent_id)))

(defmethod insert-record Node [record]
  {"name" (:name record)
   "create_time" (or (:create-time record) (util/current-timestamp))
   "parent_id" (let [parent (:parent-node record)]
                   (cond
                     (nil? parent) 0
                     (integer? parent) parent
                     (instance? Node parent) (:id parent)
                     :else (throw (Exception. "Unkonw type of parent-node."))))})

