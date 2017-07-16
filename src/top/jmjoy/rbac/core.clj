(ns top.jmjoy.rbac.core
  (:require [migratus.core :as migratus]
            [clojure.java.jdbc :as jdbc]))

(defn- get-jdbc-url []
  (or (get (System/getenv) "RBAC_DATABASE_URL")
      (throw (Exception. "Please specify RBAC_DATABASE_URL."))))

(def ^:private jdbc-config
  {:connection-uri (get-jdbc-url)})

(def ^:private jdbc-config
  {:connection-uri (get-jdbc-url)})

(def ^:private migratus-config
  "数据库偏移配置"
  {:store :database
   :migration-dir "migrations/"
   :init-script "init.sql"
   :db (get-jdbc-url)})

(defmacro auto-migratus-funs [func-name]
  `(def ~(symbol (str "migratus-" func-name))
     (partial ~(symbol "migratus.core" func-name) migratus-config)))

;; 自动生成迁移函数
(auto-migratus-funs "migrate")
(auto-migratus-funs "create")
(auto-migratus-funs "destroy")
(auto-migratus-funs "reset")
