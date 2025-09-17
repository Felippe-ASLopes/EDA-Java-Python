from fastapi import FastAPI
from typing import List
from pydantic import BaseModel
import uvicorn
import pika
import threading
import json
import time
import os

app = FastAPI()

MENSAGENS_FILE = "mensagens.txt"

class Mensagem(BaseModel):
    mensagem: str

def salvar_mensagem(mensagem):
    with open(MENSAGENS_FILE, "a", encoding="utf-8") as f:
        f.write(json.dumps(mensagem) + "\n")

def consumir_continuamente():
    while True:
        try:
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(
                    host='localhost',
                    port=5672,
                    credentials=pika.PlainCredentials(username='myuser', password='secret'),
                    heartbeat=600,
                    blocked_connection_timeout=300
                )
            )
            channel = connection.channel()
            channel.queue_declare(queue='2XKO-queue', durable=True)

            def callback(ch, method, properties, body):
                conteudo = body.decode()
                # Remove aspas extras se existirem
                if conteudo.startswith('"') and conteudo.endswith('"'):
                    conteudo = conteudo[1:-1]
                mensagem = {"mensagem": conteudo}
                salvar_mensagem(mensagem)

            channel.basic_consume(queue='2XKO-queue', on_message_callback=callback, auto_ack=True)
            channel.start_consuming()
        except Exception as e:
            print(f"Erro no consumidor RabbitMQ: {e}")
            time.sleep(5)

def ler_mensagens_do_arquivo():
    mensagens = []
    if os.path.exists(MENSAGENS_FILE):
        with open(MENSAGENS_FILE, "r", encoding="utf-8") as f:
            for linha in f:
                try:
                    mensagens.append(json.loads(linha.strip()))
                except Exception:
                    pass
    return mensagens

@app.get("/api/mensagens", response_model=List[Mensagem])
def get_mensagens():
    return ler_mensagens_do_arquivo()

def iniciar_consumidor():
    t = threading.Thread(target=consumir_continuamente, daemon=True)
    t.start()

iniciar_consumidor()

if __name__ == "__main__":
    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)