(ns top.jmjoy.rbac.core
  (:require [migratus.core :as migratus]
            [clojure.java.jdbc :as jdbc]
            [top.jmjoy.rbac.config :as config]))

(defmacro auto-migratus-funs [func-name]
  `(def ~(symbol (str "migratus-" func-name))
     (partial ~(symbol "migratus.core" func-name) config/migratus-config)))

;; 自动生成迁移函数
(auto-migratus-funs "migrate")
(auto-migratus-funs "create")
(auto-migratus-funs "destroy")
(auto-migratus-funs "reset")
