![logo](logo/JavaQuarium.png)
# JavaQuarium (Benco11)

### _Implémentation de [Javaquarium](https://zestedesavoir.com/forums/sujet/447/javaquarium/)_

JavaQuarium est une implémentation de l'exercice
de [POO](https://fr.wikipedia.org/wiki/Programmation_orient%C3%A9e_objet) [Javaquarium](https://zestedesavoir.com/forums/sujet/447/javaquarium/)
en Java.
Certaines fonctionnalités ont été améliorées ou modifiées par rapport à l'exercice de base.

JavaQuarium est écrit en Java 17 et utilise la preview du pattern matching.

## Installation et Compilation

Pour installer le projet :

```bash
git clone https://github.com/Benco11-developement/JavaQuarium.git
```

Pour compiler le projet :

```bash
cd JavaQuarium
./gradlew build
```

## Execution

Il est possible de rajouter des arguments à l'exécution du programme de cette manière :

```bash
java --enable-preview -jar JavaQuarium-1.0.jar -ARGUMENT_ID ARGUMENT_VALUE
```

Les différents arguments disponibles sont :

| ID de l'argument | Valeur et type attendue                      |
|------------------|----------------------------------------------|
| -i               | Fichier aquarium d'entrée (chemin)           |
| -o               | Fichier aquarium de sortie (chemin)          |
| -r               | Nombre de rounds à simuler (entier)          |
| -oR              | Round où l'aquarium sera enregistré (entier) |

_Exemple: Simuler pendant 30 tours l'aquarium de aquarium.txt puis sauvegarder_

```bash
java --enable-preview -jar JavaQuarium-1.0.jar -i aquarium.txt -o "aquarium 2.txt" -r 30
```

### Fichiers d'entrée et de sortie

Les fichiers d'entrée et de sortie doivent avoir la forme suivante :

```
// Ceci est un commentaire

// Algues
// Format : [Nombre d'algues] algue(s) [âge] an(s) (= tours)

3 algues 2 ans
1 algue 9 ans

// Poissons
// Format : [nom], [race], [sexe], [âge]? ans (= tours), [pv]? pvs
// L'ordre est important !

Criquette, Mérou, MALE, 10 ans
pieutez, Bar, FEMALE, 3 ans, 12 pvs

// Spécifie des ajouts à ce tour précisément
===== Tour 10 =====

// Seront ajoutés à ce tour :
1 algue 4 ans
Baton, Mérou, MALE, 2 ans

===== Tour 17 =====

// Seront ajoutés à ce tour :
10 algues 8 ans
Halo, Carpe,  FEMALE
Magnifique, Thon,  FEMALE, 1 pv
Carotte, Poisson-Clown,  MALE, 11 ans


===== Tour 20 =====
// Supprime au tour 20 :
// Algues : -[x]? algue(s) [x]? an(s)

// Toutes les algues de 11 ans
-algues 11 ans

// 10 algues de 1 an
-10 algues 1 an

// 1 algue 
-1 algue

// Poissons : -poisson [n:x]?, [sp:x]?, [sx:x]?, [a:x]? 
// (n: nom, sp: sexe, sx: sexe, a: âge)

// Toutes les carpes (Halo)
-poisson sp:Carpe

// Tous les poissons avec pour nom "Magnifique"
-poisson n:Magnifique

// Tous les poissons de 14 ans (Carotte)
-poisson a:14 ans

// Tous les mâles bars
-poisson sx:MALE, sp:Bar

// Toutes les carpes de 12 ans
-poisson sp:Carpe, a:12

===== Tour 22 =====

// Retire toutes les femêles (Baton)
-poisson sx:FEMALE
```

### Table

| Instruction | Description, Arguments                                                                                                                                                     |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| -poisson    | Retire des poissons selon certains filtres :<br/>**n:x** -> Nom du poisson<br/>**sp:x** -> Espèce du poisson<br/>**sx:x** -> Sexe du poisson<br/>**a:x** -> Âge du poisson |
| -algue      | Retire des algues selon certains filtres :<br/> -**[x]**_?_ algue(s) **[x]**_?_ an(s)                                                                                      |
| algue       | Ajoute des algues :<br/>**[x]** algue(s) **[x]** an(s)                                                                                                                     |
| poisson     | Ajoute un poisson :<br/>**[nom]**, **[espèce]**, **[sexe]**, **[age]**_?_, **[pv]**_?_                                                                                     |

### Modifications

Liste des modifications par rapport à l'énoncé original :

- Le sexe et le nombre de pvs sont spécifiés dans les fichiers aquarium
- Le nom des nouveaux-nés est sélectionné à partir du nom du parent qui est allé se reproduire dans le dictionnaire français
- Le nombre de poissons est affiché à la fin de chaque tour
- À la fin de chaque tour, pour chaque poisson, le nom, l'âge, le sexe, la race et le nombre de pvs sont affichés
- Il est possible de supprimer des êtres vivants selon certains critères
- La syntaxe des fichiers est plus souple
