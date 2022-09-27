# Compte Rendu TD1 Concurrence

Mathis MENAA

### Exercice 1 :

###### Rappeler à quoi sert un Runnable ?

Un runnable est une interface fonctionnel de la forme : () -> Void.
Utilisé pour créer le code exécuté par le thread.

###### Exécutez le programme plusieurs fois, que remarque-t-on ? Puis, en regardant l'affichage (scroller au besoin), qu'y a-t-il de bizarre ? Est-ce que tout ceci est bien normal ?

L'exécution des threads n'est pas ordonné.
On ne control pas l'ordre d'éxécution ni le temps durant lequel il va s'executer (cf scheduler de l'OS).

### Exercice 3 :

###### Observer l'évolution du nombre de threads. Que devient le thread main ? Quand est-ce que la JVM s'éteint ?

Le thread main exécute le lancement des autres threads. Un fois fais le thread main s'arrete puis au fur est a mesure que les autres threads termine, le nombre de thread total diminue.

La JVM s'éteint à la fin de l'exécution des threads en cours.


### Exercice 4 :

###### Expliquer le comportement observé. Pourquoi ce comportement n’apparaît-il pas quand on utilise System.out.println ?

On observe l'apparition de caractère qui se mélange dans l'affichage.
Ce comportement n'apparait pas avec le __System.out.println__ car il implémente la gestion de verrou.