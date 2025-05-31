from pydantic import BaseModel


class FormularioInput(BaseModel):
    study_hours_per_day: float
    extracurricular_hours_per_day: float
    sleep_hours_per_day: float
    social_hours_per_day: float
    physical_activity_hours_per_day: float
    gpa: float
