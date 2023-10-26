from typing import Union

from fastapi import FastAPI

app = FastAPI()


@app.get("/")
def read_root():
    return {"Hello": "jenkinsWorld"}

@app.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}

@app.get("/test")
def test_jenkins():
    return {"test": "great"}

@app.get("/a106/{name}/{status}")
def read_name(name: str, status: str):
    return {"상태 :" : f"A106 팀의 {name}이(가) {status} 입니다."}