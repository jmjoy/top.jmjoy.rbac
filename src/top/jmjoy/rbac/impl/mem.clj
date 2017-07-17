(ns top.jmjoy.rbac.impl.mem
  (:require [top.jmjoy.rbac.protocol :as p]))

(def context (atom nil))

(defrecord User [id]
  p/User
  (id [this] (:id this))
  (add-role [this ^p/Role role]
    (swap! context update-in [:user-role (.id this)]
           #(into #{} (conj % (:id role)))))
  (remove-role [this ^p/Role role]
    (swap! context update-in [:user-role (.id this)]
           disj (:id role))))

(defn init-context []
  {:user-role {}
   :role-operation {}})

(defn reset-context! []
  (reset! context (init-context)))

(defn context-add-role [context user role] nil)
