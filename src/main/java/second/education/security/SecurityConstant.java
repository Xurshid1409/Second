package second.education.security;

public class SecurityConstant {
    public static final Long TOKEN_EXPIRE_AT = 172_000_000L ;
    public static final Long REFRESH_TOKEN_EXPIRE_AT = 604_800_800L;
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_HEADER  = "Bearer ";
    //
    public static final String USER_NOT_FOUND = "Foydalanuvchi topilmadi";
    //
    public static final String DEFAULT_ROLE = "ROLE_ABITURIYENT";
}
