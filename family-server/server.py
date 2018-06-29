from flask import Flask
from flask_cors import CORS
from flask_restful import Api

app = Flask(__name__)
api = Api(app)
CORS(app)


@app.before_request
def authenticate():
    pass
    # args = request.args
    # print(args)
    # abort(jsonify(Result(ok=False, msg="login failed")))

if __name__ == '__main__':
    app.run(debug=True, port=8082, host='0.0.0.0')