<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<title>Cadastro de Post</title>
</head>

<body>

	<div class="container">
		<div class="row">
			<div class="col-2"></div>

			<form action="/Facebook/post/save" method="GET" class="col-8">
				<h1>Cadastro de Post</h1>

				<input type="hidden" id="post_id" name="post_id"
					value="${post.id}">

				<div class="mb-3">
					<label for="post_content_id" class="form-label">Conteúdo</label>
					<textarea id="post_content_id" name="post_content" rows="5"
						class="form-control" required>${post.content}</textarea>
				</div>

				<div class="mb-3">
					<label for="user_id" class="form-label">Usuário</label>
					<select name="user_id" id="user_id" class="form-select" required>
						<option value="">Selecione um usuário</option>
						<c:forEach var="usuario" items="${usuarios}">
							<option value="${usuario.id}"
								${post.user != null && post.user.id == usuario.id ? "selected" : ""}>
								${usuario.name}
							</option>
						</c:forEach>
					</select>
				</div>

				<button type="submit" class="btn btn-primary">Enviar</button>
			</form>

			<div class="col-2"></div>
		</div>
	</div>

	<script src="js/bootstrap.bundle.min.js"></script>
</body>

</html>
