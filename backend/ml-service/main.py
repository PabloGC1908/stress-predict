from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware
import joblib

from models.FormularioInput import FormularioInput

app = FastAPI()
model = joblib.load("./model/stress_model.joblib")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],  # o ["*"] si estás en dev
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.post("/predict")
async def predecir_estres(formulario: FormularioInput):
    input_data = [[
        formulario.study_hours_per_day,
        formulario.extracurricular_hours_per_day,
        formulario.sleep_hours_per_day,
        formulario.social_hours_per_day,
        formulario.physical_activity_hours_per_day,
        formulario.gpa
    ]]

    prediccion = model.predict(input_data)

    if prediccion == "Low":
        return "Bajo estrés"
    elif prediccion == "Moderate":
        return "Bajo estrés"
    else:
        return "Bajo estrés"


