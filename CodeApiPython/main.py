from pathlib import Path
import pickle

import pandas as pd
from fastapi import FastAPI, HTTPException

from data_loader import load_training_data
from models import Interaction, UserData
from recommendation import entrainer_modele, recommander

app = FastAPI()
chemin_modele = Path(__file__).parent / "knn_model.pkl"
modele = None
donnees = None
if chemin_modele.exists():
    with chemin_modele.open("rb") as fichier:
        modele = pickle.load(fichier)


def verifier_modele():
    if modele is None:
        raise HTTPException(
            status_code=503,
            detail="Le modèle de recommandation est indisponible.",
        )
    return modele

# Endpoint de base pour tester que l'API est en ligne
@app.get("/")
async def root():
    return {"message": "API de recommandation en ligne"}

# Endpoint pour envoyer les données d'utilisateur et récupérer des recommandations
@app.post("/recommendations/")
async def get_recommendations(data: UserData):
    return {"recommendations": recommander(data.user_id, verifier_modele())}

@app.get("/recommandations/{idUtilisateur}")
async def recommandations(idUtilisateur: int):
    return recommander(idUtilisateur, verifier_modele())

@app.post("/interactions")
async def ajouter_interaction(interaction: Interaction):
    global donnees, modele
    verifier_modele()
    if donnees is None:
        donnees = load_training_data(Path(__file__).parent / "data" / "interactions.csv")
    nouvelle_ligne = interaction.model_dump()
    donnees = pd.concat(
        [donnees, pd.DataFrame([nouvelle_ligne])],
        ignore_index=True,
    )
    modele = entrainer_modele(donnees)
    return interaction
