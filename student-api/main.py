from fastapi import FastAPI
from fastapi import Depends
from fastapi import HTTPException
from fastapi import status
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session

from database import Base
from database import engine
from database import get_db

from auth import authenticate_user
from auth import create_access_token
from auth import get_current_user

from models import Student
from schemas import StudentCreate
from schemas import StudentResponse
from schemas import Token


Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Student Management API",
    version="1.0"
)


@app.get("/")
def root():
    return {
        "message": "Student Management API"
    }


@app.post("/token", response_model=Token)
def login(
    form_data: OAuth2PasswordRequestForm = Depends(),
    db: Session = Depends(get_db)
):
    user = authenticate_user(db, form_data.username, form_data.password)

    if user is None:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )

    return {
        "access_token": create_access_token(user.username),
        "token_type": "bearer"
    }


@app.get(
    "/students",
    dependencies=[Depends(get_current_user)],
    response_model=list[StudentResponse]
)
def get_students(
    db: Session = Depends(get_db)
):
    return db.query(Student).all()


@app.get(
    "/students/{student_id}",
    dependencies=[Depends(get_current_user)],
    response_model=StudentResponse
)
def get_student(
    student_id: int,
    db: Session = Depends(get_db)
):
    return db.query(Student).filter(
        Student.id == student_id
    ).first()


@app.post(
    "/students",
    dependencies=[Depends(get_current_user)],
    response_model=StudentResponse
)
def create_student(
    student: StudentCreate,
    db: Session = Depends(get_db)
):
    new_student = Student(
        student_code=student.student_code,
        name=student.name,
        major=student.major
    )

    db.add(new_student)
    db.commit()
    db.refresh(new_student)

    return new_student


@app.put(
    "/students/{student_id}",
    dependencies=[Depends(get_current_user)],
    response_model=StudentResponse
)
def update_student(
    student_id: int,
    student: StudentCreate,
    db: Session = Depends(get_db)
):
    existing = db.query(Student).filter(
        Student.id == student_id
    ).first()

    if existing is None:
        raise HTTPException(
            status_code=404,
            detail="Student not found"
        )

    existing.student_code = student.student_code
    existing.name = student.name
    existing.major = student.major

    db.commit()
    db.refresh(existing)

    return existing


@app.delete(
    "/students/{student_id}",
    dependencies=[Depends(get_current_user)]
)
def delete_student(
    student_id: int,
    db: Session = Depends(get_db)
):
    student = db.query(Student).filter(
        Student.id == student_id
    ).first()

    if student is None:
        raise HTTPException(
            status_code=404,
            detail="Student not found"
        )

    db.delete(student)
    db.commit()

    return {
        "message": "Student deleted"
    }
