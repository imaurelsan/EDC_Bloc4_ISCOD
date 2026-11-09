import asyncio
import unittest
from unittest.mock import patch

import pandas as pd

import main
from models import UserData
from recommendation import entrainer_modele


class RecommendationRouteTest(unittest.TestCase):

    def test_post_recommendations_utilise_le_modele_knn(self):
        donnees = pd.DataFrame([
            {"user_id": 1, "game_id": 1, "interaction": 1},
            {"user_id": 2, "game_id": 1, "interaction": 1},
            {"user_id": 2, "game_id": 2, "interaction": 1},
        ])
        modele = entrainer_modele(donnees)
        data = UserData(user_id=1, purchases=[])

        with patch.object(main, "modele", modele):
            resultat = asyncio.run(main.get_recommendations(data))

        self.assertEqual({"recommendations": [2]}, resultat)


if __name__ == "__main__":
    unittest.main()
