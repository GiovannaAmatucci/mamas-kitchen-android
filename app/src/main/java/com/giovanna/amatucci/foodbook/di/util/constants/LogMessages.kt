package com.giovanna.amatucci.foodbook.di.util.constants

object LogMessages {

    // --- CAMADA DE AUTENTICAÇÃO (AuthApi, AuthRepository) ---
    const val AUTH_TOKEN_REQUEST = "Iniciando requisição de token de acesso..."
    const val AUTH_TOKEN_SUCCESS = "Token de acesso recebido da API."
    const val AUTH_TOKEN_FAILURE = "Falha ao buscar token de acesso da API. Causa: %s"
    const val AUTH_TOKEN_SAVE_FAILURE =
        "API buscou o token, mas falhou ao salvar no repositório. Causa: %s"


    // --- REPOSITÓRIO DE TOKEN (TokenRepository) ---
    const val TOKEN_REPO_SAVE_START = "Salvando token no repositório..."
    const val TOKEN_REPO_SAVE_SUCCESS = "Token salvo no DB com sucesso. Expira em: %s"
    const val TOKEN_REPO_SAVE_FAILURE = "Falha ao salvar/criptografar token no DB. Causa: %s"
    const val TOKEN_REPO_GET_START = "Buscando token válido do repositório..."
    const val TOKEN_REPO_GET_SUCCESS = "Token válido encontrado no DB e descriptografado."
    const val TOKEN_REPO_GET_NOT_FOUND = "Nenhum token encontrado no DB."
    const val TOKEN_REPO_GET_EXPIRED = "Token encontrado no DB, mas está expirado."
    const val TOKEN_REPO_DECRYPT_FAILURE =
        "Falha ao descriptografar token. Chave pode ter sido invalidada. Causa: %s"
    const val TOKEN_REPO_CLEAR = "Limpando token do repositório."
    const val TOKEN_REPO_CLEAR_FAILURE = "Falha ao limpar token do DB. Causa: %s"


    // --- CLIENTE KTOR (Plugin de Auth) ---
    const val KTOR_LOAD_START = "loadTokens: Verificando token no DB..."
    const val KTOR_LOAD_SUCCESS =
        "loadTokens: Token válido encontrado no DB. Usando para a chamada."
    const val KTOR_LOAD_FAILURE =
        "loadTokens: Nenhum token válido no DB. Ktor irá acionar 'refreshTokens'."
    const val KTOR_REFRESH_START =
        "refreshTokens: Bloco ativado (401 ou token ausente). Iniciando tentativa de refresh..."
    const val KTOR_REFRESH_SUCCESS =
        "refreshTokens: Token renovado com sucesso. Retentando chamada original."
    const val KTOR_REFRESH_FAILURE =
        "refreshTokens: FALHA ao renovar o token. Limpando credenciais."
    const val KTOR_REFRESH_MUTEX_EXECUTE =
        "refreshTokens: Este request obteve o Mutex e fará a chamada de refresh."
    const val KTOR_REFRESH_MUTEX_WAIT =
        "refreshTokens: Token já atualizado por outra thread. Usando token do DB."


    // --- API (BaseApi - safeApiCall) ---
    const val API_ERROR_CLIENT = "Erro do Cliente (4xx): %s"
    const val API_ERROR_SERVER = "Erro do Servidor (5xx): %s"
    const val API_ERROR_NETWORK = "Erro de Rede/IO: %s"
    const val API_ERROR_SERIALIZATION = "Erro de Serialização: %s"
    const val API_ERROR_UNEXPECTED = "Erro Inesperado: %s"


    // --- API DE RECEITAS (FatSecretRecipeApi) ---
    const val API_RECIPE_SEARCH = "Buscando receitas. Query: '%s', Página: %s"
    const val API_RECIPE_DETAILS = "Buscando detalhes da receita. ID: %s"


    // --- PAGING SOURCE (RecipePagingSource) ---
    const val PAGING_LOAD_API_ERROR = "Erro de API no PagingSource. Código: %d, Mensagem: %s"
    const val PAGING_LOAD_API_EXCEPTION = "Exceção da API no PagingSource: %s"
    const val PAGING_LOAD_UNKNOWN_ERROR = "Erro inesperado no PagingSource.load: %s"


    // --- REPOSITÓRIO DE RECEITAS (RecipeRepository) ---
    const val REPO_PAGER_CREATED = "Pager e PagingSource criados para a query: '%s'"
    const val REPO_DETAILS_REQUEST = "Buscando detalhes no repositório para o ID: %s"
    const val REPO_DETAILS_SUCCESS = "Detalhes encontrados e mapeados para o domínio. ID: %s"
    const val REPO_DETAILS_API_ERROR = "Erro da API ao buscar detalhes. Código: %s, Mensagem: %s"
    const val REPO_DETAILS_API_EXCEPTION = "Exceção da API ao buscar detalhes: %s"
    const val REPO_DETAILS_MAPPER_FAILURE =
        "Sucesso da API, mas falha ao mapear DTO -> Domain. Causa: %s"



    const val REPO_FAVORITE_ADD_START = "Adicionando favorito ao repositório. ID: %s"
    const val REPO_FAVORITE_REMOVE_START = "Removendo favorito do repositório. ID: %s"

    // --- REPOSITÓRIO DE PESQUISA (SearchRepository) ---

    const val SAVE_QUERY_FAILURE = "Falha ao salvar a query de busca: %s"
    const val GET_QUERIES_FAILURE = "Falha ao obter o histórico de buscas"
    const val CLEAR_HISTORY_FAILURE = "Falha ao limpar o histórico de buscas"


}