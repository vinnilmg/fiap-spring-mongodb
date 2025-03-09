-- Insere um artigo
db.artigo.insertOne({ titulo: "Teste", data: new Date(), texto: "Teste", url: "http://", status: 1 })

-- Insere um autor
db.autor.insertOne({ nome: "Vinicius", bio: "História", imagem: "jpg" })

-- Insere um autor no artigo
db.artigo.updateOne({ _id: ObjectId("67c79235ba3ed1dda1cb0ce2") }, { $set: { autor: ObjectId("67c792caba3ed1dda1cb0ce3") }})

-- Insere um nome ao autor do artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$set: {autor: {_id: ObjectId("67c792caba3ed1dda1cb0ce3"), nome: "Vinicius"}}})

-- Insere categorias
db.categoria.insertMany([{nome: "cat1", descricao: "desc1"}, {nome: "cat2", descricao: "desc2"}])

-- Insere uma categoria no artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$set: {categorias: [{_id: ObjectId("67c795e4ba3ed1dda1cb0ce4"), nome: "cat1"}]}})

-- Insere tags no artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$set: {tags: ["Tag1", "Tag2"]}})

-- Insere uma lista vazia de comentarios no artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$set: {comentarios: []}})

-- Insere um comentario no artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$push: {comentarios: {nome: "Hater", texto: "Hello hate!", data: new Date()}}})

-- Procura artigos por url
db.artigo.find({url: "http://"})

-- Remove um comentário de um artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$pull: {comentarios: {nome: "Hater", texto: "Hello hate2!"}}})

-- Altera o comentário de um artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$set: {comentarios: {nome: "Vini", texto: "Ola pessoal!", data: new Date()}}})

-- Procura artigos por nome do autor
db.artigo.find({"autor.nome": "Vinicius"})

-- Procura artigos por nome de um comentário
db.artigo.find({"comentarios.nome": "Vini"})

-- Procura artigos por uma tag
db.artigo.find({tags: "Tag2"})

-- Atualiza o nome do autor de um artigo
db.artigo.updateOne({_id: ObjectId("67c79235ba3ed1dda1cb0ce2")}, {$set: {"autor.nome": "Vinicius de Lima"}})