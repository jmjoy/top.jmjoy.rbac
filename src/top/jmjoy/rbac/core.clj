(ns top.jmjoy.rbac.core
  (:require [migratus.core :as migratus]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [unilog.config :refer [start-logging!]]
            [clojure.test :as test]
            [top.jmjoy.rbac.config :as config])
  (:gen-class))

(start-logging! {:level "debug" :console true})

(defmacro auto-migratus-funs [func-name]
  `(def ~(symbol (str "-migratus-" func-name))
     (partial ~(symbol "migratus.core" func-name) config/migratus-config)))

;; 自动生成迁移函数
(auto-migratus-funs "migrate")
(auto-migratus-funs "create")
(auto-migratus-funs "destroy")
(auto-migratus-funs "reset")


(defn -run-tests
  "运行测试"
  []
  (let [names ["core" "model"]
        prefix-ns "top.jmjoy.rbac.test."
        symbols (map #(symbol (str prefix-ns %)) names)]
    (doseq [sym symbols]
      (require :reload sym))
    (apply test/run-tests symbols)))


(defn -main [& args]
  (log/info "Runned."))
