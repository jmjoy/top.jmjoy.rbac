(ns top.jmjoy.rbac.util)

(defn println-eval [& args]
  (when-not (empty? args)
    (let [v (eval args)]
      (println v)
      v)))

(defn current-timestamp []
  (quot (System/currentTimeMillis) 1000))

