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
java --enable-preview -jar JavaQuarium-1.0.jar -ARGUMENT_ID 'ARGUMENT_VALUE'
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
java --enable-preview -jar JavaQuarium-1.0.jar -i aquarium.txt -o aquarium.txt -r 30
```

### Fichiers d'entrées et de sorties

Les fichiers d'entrées et de sorties doivent avoir la forme suivante :

```
// Ceci est un commentaire

// Algues
// Format : [Nombre d'algues] algues [âge] ans (= tours)

3 algues 2 ans
1 algues 9 ans

// Poissons
// Format : [nom], [race], [sexe], [âge] ans (= tours)

Criquette, Mérou, MALE, 10 ans
pieutez, Bar, FEMALE, 3 ans

// Spécifie des ajouts à ce tour précisément
===== Tour 10 =====

// Seront ajoutés à ce tour :
1 algues 4 ans
Baton, Mérou, MALE, 2 ans

```

### Modifications

Liste des modifications par rapport à l'énoncé original :

- Le sexe est spécifié dans les fichiers aquarium
- Le nom des nouveaux-nés est sélectionné à partir du nom du parent qui est allé se reproduire dans le dictionnaire
  français
- Le nombre de poissons est affiché à la fin de chaque tour
- À la fin de chaque tour, pour chaque poisson, le nom, l'âge, le sexe, la race et le nombre de pv sont affichés
