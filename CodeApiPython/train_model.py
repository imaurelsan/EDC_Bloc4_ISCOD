from pathlib import Path
import pickle
import sys

import pandas as pd
from sklearn.neighbors import NearestNeighbors


def train_model(csv_path: str, model_path: str) -> None:
    data = pd.read_csv(csv_path, comment="#")
    value_column = "interaction" if "interaction" in data.columns else "note"
    matrix = data.pivot_table(
        index="user_id",
        columns="game_id",
        values=value_column,
        aggfunc="max",
        fill_value=0,
    )
    model = NearestNeighbors(metric="cosine")
    model.fit(matrix)

    with open(model_path, "wb") as file:
        pickle.dump({"matrice": matrix, "jeux": list(matrix.columns), "knn": model}, file)


if __name__ == "__main__":
    base_path = Path(__file__).parent
    csv_path = sys.argv[1] if len(sys.argv) > 1 else str(base_path / "data" / "interactions.csv")
    model_path = sys.argv[2] if len(sys.argv) > 2 else str(base_path / "data" / "knn_model.pkl")
    train_model(
        csv_path,
        model_path,
    )
