(ns top.jmjoy.rbac.util)

(defmacro ep
  "Print and return result by evaling remained args."
  [& args]
  (when-not (empty? args)
    `(let [v# (do ~@args)] (println v#) v#)))

(defn current-timestamp
  "Get current integer timestamp."
  []
  (quot (System/currentTimeMillis) 1000))
