(ns top.jmjoy.rbac.util)

(defn println-eval
  "Print and return result by evaling remained args."
  [& args]
  (when-not (empty? args)
    (let [v (eval args)]
      (println v)
      v)))

(defn current-timestamp
  "Get current integer timestamp."
  []
  (quot (System/currentTimeMillis) 1000))

(defn db-truncate
  [table-name]
  (jdbc/execute! config/jdbc-config (format "truncate `%s`" table-name)))

(defn )
