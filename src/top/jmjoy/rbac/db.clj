(ns top.jmjoy.rbac.db
  (:require [clojure.java.jdbc :as jdbc]
            [top.jmjoy.rbac.config :as config]))

(def insert! (partial jdbc/insert! config/jdbc-config))

(def execute! (partial jdbc/execute! config/jdbc-config))

(defn truncate! [table-name]
  (execute! (format "TRUNCATE `%s`" table-name)))

(def query (partial jdbc/query config/jdbc-config))

(def query-one (comp first query))
