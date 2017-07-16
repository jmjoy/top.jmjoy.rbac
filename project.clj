(defproject top.jmjoy/rbac "0.1.0-SNAPSHOT"
  :description " An abstract RBAC implemented for clojure."
  :url "https://github.com/jmjoy/top.jmjoy.rbac"
  :license {:name "MIT"
            :url "https://github.com/jmjoy/top.jmjoy.rbac/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.7.0-beta5"]
                 [mysql/mysql-connector-java "5.1.41"]
                 [migratus "0.9.7"]
                 [korma "0.4.3"]
                 [lein-dotenv "1.0.0"]]
  :repl-options {:init-ns top.jmjoy.rbac.core}
  :plugins [[migratus-lein "0.5.0"]
            [lein-dotenv "1.0.0"]]
  :migratus {:store :database
             :migration-dir "migrations"
             :db ~(or (get (System/getenv) "RBAC_DATABASE_URL")
                      (throw (Exception. "Please specify RBAC_DATABASE_URL.")))})
