import os
import sys

from dotenv import load_dotenv

from auth import hash_password
from database import Base, SessionLocal, engine
from models import User

load_dotenv()

username = sys.argv[1] if len(sys.argv) > 1 else os.getenv("SEED_USERNAME")
password = sys.argv[2] if len(sys.argv) > 2 else os.getenv("SEED_PASSWORD")

if not username or not password:
    sys.exit("Usage: python seed_user.py <username> <password>  (or set SEED_USERNAME/SEED_PASSWORD in .env)")

Base.metadata.create_all(bind=engine)

db = SessionLocal()
try:
    user = db.query(User).filter(User.username == username).first()
    if user is None:
        db.add(User(username=username, hashed_password=hash_password(password)))
        print(f"created user '{username}'")
    else:
        user.hashed_password = hash_password(password)
        print(f"updated password for '{username}'")
    db.commit()
finally:
    db.close()
