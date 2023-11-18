FROM python:3.9
 
RUN mkdir /app
WORKDIR /app

COPY ./requirements.txt ./requirements.txt
RUN pip install --no-cache-dir --upgrade -r requirements.txt
RUN apt-get update -y && apt-get install -y ffmpeg

COPY ./src ./src
CMD ["uvicorn", "src.main:app", "--host", "0.0.0.0", "--port", "8002"]
