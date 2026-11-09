from typing import Dict, List

import pandas as pd
from sklearn.neighbors import NearestNeighbors

from models import UserData

def entrainer_modele(df: pd.DataFrame) -> Dict:
    # Prépare une matrice avec une ligne par utilisateur et une colonne par jeu.
    if df.empty:
        return {"matrice": pd.DataFrame(), "jeux": [], "knn": None}

    matrice = df.pivot_table(
        index="user_id",
        columns="game_id",
        values="interaction",
        aggfunc="max",
        fill_value=0,
    )
    knn = NearestNeighbors(metric="cosine")
    knn.fit(matrice)
    return {"matrice": matrice, "jeux": list(matrice.columns), "knn": knn}

def recommander(idUtilisateur: int, modele: Dict, nb: int = 5) -> List[int]:
    # Retourne seulement des jeux que l'utilisateur n'a pas encore consommés.
    matrice = modele["matrice"]
    if modele["knn"] is None or idUtilisateur not in matrice.index:
        return []

    ligne = matrice.loc[[idUtilisateur]]
    nombre_voisins = min(nb + 1, len(matrice))
    _, voisins = modele["knn"].kneighbors(ligne, n_neighbors=nombre_voisins)
    jeux_vus = set(matrice.loc[idUtilisateur][matrice.loc[idUtilisateur] > 0].index)
    resultats = []
    for voisin in voisins[0]:
        for jeu in matrice.iloc[voisin][matrice.iloc[voisin] > 0].index:
            if jeu not in jeux_vus and jeu not in resultats:
                resultats.append(int(jeu))
            if len(resultats) == nb:
                return resultats
    return resultats

def generate_recommendations(user_data: UserData):
    # Garde l'ancien format de réponse de l'API.
    return [
        {"game_id": jeu.game_id, "game_name": ""}
        for jeu in user_data.purchases
    ]
