(ns top.jmjoy.rbac.config)

(defn- get-jdbc-url []
  (or (get (System/getenv) "RBAC_DATABASE_URL")
      (throw (Exception. "Please specify RBAC_DATABASE_URL."))))

(def jdbc-config
  "java.jdbc配置"
  {:connection-uri (get-jdbc-url)})

(def migratus-config
  "数据库偏移配置"
  {:store :database
   :migration-dir "migrations/"
   :init-script "init.sql"
   :db (get-jdbc-url)})
