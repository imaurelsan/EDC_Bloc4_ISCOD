import pandas as pd
from pathlib import Path

def load_training_data(file_path="data/interactions.csv"):
    # Crée le fichier avec ses colonnes si besoin.
    chemin = Path(file_path)
    chemin.parent.mkdir(parents=True, exist_ok=True)
    if not chemin.exists():
        pd.DataFrame(columns=["user_id", "game_id", "interaction"]).to_csv(chemin, index=False)
    return pd.read_csv(chemin)
