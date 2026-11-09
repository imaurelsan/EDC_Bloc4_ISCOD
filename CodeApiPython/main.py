from pathlib import Path

import pandas as pd
from fastapi import FastAPI, HTTPException

from data_loader import load_training_data
from models import Interaction, UserData
from recommendation import entrainer_modele, generate_recommendations, recommander

app = FastAPI()
donnees = load_training_data(Path(__file__).parent / "data" / "interactions.csv")
modele = entrainer_modele(donnees)

# Endpoint de base pour tester que l'API est en ligne
@app.get("/")
async def root():
    return {"message": "API de recommandation en ligne"}

# Endpoint pour envoyer les données d'utilisateur et récupérer des recommandations
@app.post("/recommendations/")
async def get_recommendations(data: UserData):
    try:
        recommendations = generate_recommendations(data)
        return {"recommendations": recommendations}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/recommandations/{idUtilisateur}")
async def recommandations(idUtilisateur: int):
    return recommander(idUtilisateur, modele)

@app.post("/interactions")
async def ajouter_interaction(interaction: Interaction):
    global donnees, modele
    nouvelle_ligne = interaction.model_dump()
    donnees = pd.concat(
        [donnees, pd.DataFrame([nouvelle_ligne])],
        ignore_index=True,
    )
    modele = entrainer_modele(donnees)
    return interaction
