(ns top.jmjoy.rbac.model
  (:require [clojure.java.jdbc :as jdbc]
            [top.jmjoy.rbac.config :as config]
            [top.jmjoy.rbac.util :as util]))

(defrecord User [id name create-time])

(defn user-add [name]
  (let [create-time (util/current-timestamp)
        [{id :generated_key}] (jdbc/insert!
                               config/jdbc-config "user"
                               {"name" name
                                "create_time" create-time})]
    (User. id name create-time)))

(defrecord Role [id name create-time parent-role])

(defn role-add
  ([name] (role-add name nil))
  ([name parent-role]
   (let [create-time (util/current-timestamp)
         parent-id (if parent-role (:id parent-role) 0)
         [{id :generated_key}] (jdbc/insert!
                                config/jdbc-config "role"
                                {"name" name
                                 "parent_id" parent-id
                                 "create_time" create-time})]
     (Role. id name create-time parent-role))))
