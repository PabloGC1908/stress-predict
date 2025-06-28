from urllib.request import Request

from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware
import joblib

from models.FormularioInput import FormularioInput
from kafka import KafkaProducer
import json

producer = KafkaProducer(
    bootstrap_servers='localhost:9092',
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

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


@app.post("/api/ml/predict")
async def predecir_estres(
        request: Request,
        formulario: FormularioInput):
    token = request.headers.get("authorization")

    input_data = [[
        formulario.study_hours_per_day,
        formulario.extracurricular_hours_per_day,
        formulario.sleep_hours_per_day,
        formulario.social_hours_per_day,
        formulario.physical_activity_hours_per_day,
        formulario.gpa
    ]]

    prediccion = model.predict(input_data)[0]

    resultado = {
        "entrada": {
            "horas_estudio": formulario.study_hours_per_day,
            "horas_extracurriculares": formulario.extracurricular_hours_per_day,
            "horas_sueno": formulario.sleep_hours_per_day,
            "horas_sociales": formulario.social_hours_per_day,
            "actividad_fisica": formulario.physical_activity_hours_per_day,
            "promedio_calificaciones": formulario.gpa
        },
        "jwt": token,
        "prediccion": prediccion
    }

    # Enviamos a Kafka
    producer.send("predicciones-estres", value=resultado)
    producer.flush()

    if prediccion == "Low":
        return "Bajo estrés"
    elif prediccion == "Moderate":
        return "Estrés moderado"
    else:
        return "Alto estrés"


