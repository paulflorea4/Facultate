	.file	"turns.c"
	.text
.Ltext0:
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC0:
	.string	"a"
	.text
	.globl	fa
	.type	fa, @function
fa:
.LVL0:
.LFB14:
	.file 1 "turns.c"
	.loc 1 7 19 view -0
	.cfi_startproc
	.loc 1 7 19 is_stmt 0 view .LVU1
	pushq	%rbx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	.loc 1 8 5 is_stmt 1 view .LVU2
.LVL1:
	.loc 1 9 5 view .LVU3
	.loc 1 8 9 is_stmt 0 view .LVU4
	movl	$0, %ebx
.LVL2:
.L3:
	.loc 1 9 10 is_stmt 1 view .LVU5
	cmpl	$100, %ebx
	je	.L7
	.loc 1 11 9 view .LVU6
	.loc 1 11 17 is_stmt 0 view .LVU7
	movl	turn(%rip), %eax
	.loc 1 11 11 view .LVU8
	testl	%eax, %eax
	jne	.L3
	.loc 1 12 13 is_stmt 1 view .LVU9
	movl	$.LC0, %edi
	call	puts
.LVL3:
	.loc 1 13 13 view .LVU10
	.loc 1 13 14 is_stmt 0 view .LVU11
	addl	$1, %ebx
.LVL4:
	.loc 1 14 13 is_stmt 1 view .LVU12
	.loc 1 14 18 is_stmt 0 view .LVU13
	movl	$1, turn(%rip)
	jmp	.L3
.L7:
	.loc 1 18 5 is_stmt 1 view .LVU14
	.loc 1 19 1 is_stmt 0 view .LVU15
	movl	$0, %eax
	popq	%rbx
	.cfi_def_cfa_offset 8
.LVL5:
	.loc 1 19 1 view .LVU16
	ret
	.cfi_endproc
.LFE14:
	.size	fa, .-fa
	.section	.rodata.str1.1
.LC1:
	.string	"b"
	.text
	.globl	fb
	.type	fb, @function
fb:
.LVL6:
.LFB15:
	.loc 1 21 19 is_stmt 1 view -0
	.cfi_startproc
	.loc 1 21 19 is_stmt 0 view .LVU18
	pushq	%rbx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	.loc 1 22 5 is_stmt 1 view .LVU19
.LVL7:
	.loc 1 23 5 view .LVU20
	.loc 1 22 9 is_stmt 0 view .LVU21
	movl	$0, %ebx
.LVL8:
.L10:
	.loc 1 23 10 is_stmt 1 view .LVU22
	cmpl	$100, %ebx
	je	.L14
	.loc 1 25 9 view .LVU23
	.loc 1 25 17 is_stmt 0 view .LVU24
	movl	turn(%rip), %eax
	.loc 1 25 11 view .LVU25
	cmpl	$1, %eax
	jne	.L10
	.loc 1 26 13 is_stmt 1 view .LVU26
	movl	$.LC1, %edi
	call	puts
.LVL9:
	.loc 1 27 13 view .LVU27
	.loc 1 27 14 is_stmt 0 view .LVU28
	addl	$1, %ebx
.LVL10:
	.loc 1 28 13 is_stmt 1 view .LVU29
	.loc 1 28 18 is_stmt 0 view .LVU30
	movl	$0, turn(%rip)
	jmp	.L10
.L14:
	.loc 1 32 5 is_stmt 1 view .LVU31
	.loc 1 33 1 is_stmt 0 view .LVU32
	movl	$0, %eax
	popq	%rbx
	.cfi_def_cfa_offset 8
.LVL11:
	.loc 1 33 1 view .LVU33
	ret
	.cfi_endproc
.LFE15:
	.size	fb, .-fb
	.globl	main
	.type	main, @function
main:
.LVL12:
.LFB16:
	.loc 1 35 33 is_stmt 1 view -0
	.cfi_startproc
	.loc 1 35 33 is_stmt 0 view .LVU35
	subq	$24, %rsp
	.cfi_def_cfa_offset 32
	.loc 1 36 5 is_stmt 1 view .LVU36
	.loc 1 37 5 view .LVU37
	movl	$0, %esi
.LVL13:
	.loc 1 37 5 is_stmt 0 view .LVU38
	movl	$m, %edi
.LVL14:
	.loc 1 37 5 view .LVU39
	call	pthread_mutex_init
.LVL15:
	.loc 1 38 5 is_stmt 1 view .LVU40
	movl	$0, %ecx
	movl	$fa, %edx
	movl	$0, %esi
	leaq	8(%rsp), %rdi
	call	pthread_create
.LVL16:
	.loc 1 39 5 view .LVU41
	movl	$0, %ecx
	movl	$fb, %edx
	movl	$0, %esi
	movq	%rsp, %rdi
	call	pthread_create
.LVL17:
	.loc 1 40 5 view .LVU42
	movl	$0, %esi
	movq	8(%rsp), %rdi
	call	pthread_join
.LVL18:
	.loc 1 41 5 view .LVU43
	movl	$0, %esi
	movq	(%rsp), %rdi
	call	pthread_join
.LVL19:
	.loc 1 42 5 view .LVU44
	movl	$m, %edi
	call	pthread_mutex_destroy
.LVL20:
	.loc 1 43 5 view .LVU45
	.loc 1 44 1 is_stmt 0 view .LVU46
	movl	$0, %eax
	addq	$24, %rsp
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE16:
	.size	main, .-main
	.comm	m,40,32
	.globl	turn
	.bss
	.align 4
	.type	turn, @object
	.size	turn, 4
turn:
	.zero	4
	.text
.Letext0:
	.file 2 "/opt/rh/devtoolset-9/root/usr/lib/gcc/x86_64-redhat-linux/9/include/stddef.h"
	.file 3 "/usr/include/bits/types.h"
	.file 4 "/usr/include/libio.h"
	.file 5 "/usr/include/stdio.h"
	.file 6 "/usr/include/bits/sys_errlist.h"
	.file 7 "/usr/include/time.h"
	.file 8 "/usr/include/bits/pthreadtypes.h"
	.file 9 "/usr/include/pthread.h"
	.file 10 "<built-in>"
	.section	.debug_info,"",@progbits
.Ldebug_info0:
	.long	0x703
	.value	0x4
	.long	.Ldebug_abbrev0
	.byte	0x8
	.uleb128 0x1
	.long	.LASF87
	.byte	0xc
	.long	.LASF88
	.long	.LASF89
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.long	.Ldebug_line0
	.uleb128 0x2
	.long	.LASF7
	.byte	0x2
	.byte	0xd1
	.byte	0x17
	.long	0x39
	.uleb128 0x3
	.byte	0x8
	.byte	0x7
	.long	.LASF0
	.uleb128 0x3
	.byte	0x1
	.byte	0x8
	.long	.LASF1
	.uleb128 0x3
	.byte	0x2
	.byte	0x7
	.long	.LASF2
	.uleb128 0x3
	.byte	0x4
	.byte	0x7
	.long	.LASF3
	.uleb128 0x3
	.byte	0x1
	.byte	0x6
	.long	.LASF4
	.uleb128 0x3
	.byte	0x2
	.byte	0x5
	.long	.LASF5
	.uleb128 0x4
	.byte	0x4
	.byte	0x5
	.string	"int"
	.uleb128 0x5
	.long	0x63
	.uleb128 0x3
	.byte	0x8
	.byte	0x5
	.long	.LASF6
	.uleb128 0x2
	.long	.LASF8
	.byte	0x3
	.byte	0x8c
	.byte	0x19
	.long	0x6f
	.uleb128 0x2
	.long	.LASF9
	.byte	0x3
	.byte	0x8d
	.byte	0x1b
	.long	0x6f
	.uleb128 0x6
	.byte	0x8
	.uleb128 0x7
	.byte	0x8
	.long	0x96
	.uleb128 0x3
	.byte	0x1
	.byte	0x6
	.long	.LASF10
	.uleb128 0x8
	.long	0x96
	.uleb128 0x9
	.long	.LASF40
	.byte	0xd8
	.byte	0x4
	.byte	0xf6
	.byte	0x8
	.long	0x241
	.uleb128 0xa
	.long	.LASF11
	.byte	0x4
	.byte	0xf7
	.byte	0x7
	.long	0x63
	.byte	0
	.uleb128 0xa
	.long	.LASF12
	.byte	0x4
	.byte	0xfc
	.byte	0x9
	.long	0x90
	.byte	0x8
	.uleb128 0xa
	.long	.LASF13
	.byte	0x4
	.byte	0xfd
	.byte	0x9
	.long	0x90
	.byte	0x10
	.uleb128 0xa
	.long	.LASF14
	.byte	0x4
	.byte	0xfe
	.byte	0x9
	.long	0x90
	.byte	0x18
	.uleb128 0xa
	.long	.LASF15
	.byte	0x4
	.byte	0xff
	.byte	0x9
	.long	0x90
	.byte	0x20
	.uleb128 0xb
	.long	.LASF16
	.byte	0x4
	.value	0x100
	.byte	0x9
	.long	0x90
	.byte	0x28
	.uleb128 0xb
	.long	.LASF17
	.byte	0x4
	.value	0x101
	.byte	0x9
	.long	0x90
	.byte	0x30
	.uleb128 0xb
	.long	.LASF18
	.byte	0x4
	.value	0x102
	.byte	0x9
	.long	0x90
	.byte	0x38
	.uleb128 0xb
	.long	.LASF19
	.byte	0x4
	.value	0x103
	.byte	0x9
	.long	0x90
	.byte	0x40
	.uleb128 0xb
	.long	.LASF20
	.byte	0x4
	.value	0x105
	.byte	0x9
	.long	0x90
	.byte	0x48
	.uleb128 0xb
	.long	.LASF21
	.byte	0x4
	.value	0x106
	.byte	0x9
	.long	0x90
	.byte	0x50
	.uleb128 0xb
	.long	.LASF22
	.byte	0x4
	.value	0x107
	.byte	0x9
	.long	0x90
	.byte	0x58
	.uleb128 0xb
	.long	.LASF23
	.byte	0x4
	.value	0x109
	.byte	0x16
	.long	0x27e
	.byte	0x60
	.uleb128 0xb
	.long	.LASF24
	.byte	0x4
	.value	0x10b
	.byte	0x14
	.long	0x284
	.byte	0x68
	.uleb128 0xb
	.long	.LASF25
	.byte	0x4
	.value	0x10d
	.byte	0x7
	.long	0x63
	.byte	0x70
	.uleb128 0xb
	.long	.LASF26
	.byte	0x4
	.value	0x111
	.byte	0x7
	.long	0x63
	.byte	0x74
	.uleb128 0xb
	.long	.LASF27
	.byte	0x4
	.value	0x113
	.byte	0xd
	.long	0x76
	.byte	0x78
	.uleb128 0xb
	.long	.LASF28
	.byte	0x4
	.value	0x117
	.byte	0x12
	.long	0x47
	.byte	0x80
	.uleb128 0xb
	.long	.LASF29
	.byte	0x4
	.value	0x118
	.byte	0xf
	.long	0x55
	.byte	0x82
	.uleb128 0xb
	.long	.LASF30
	.byte	0x4
	.value	0x119
	.byte	0x8
	.long	0x28a
	.byte	0x83
	.uleb128 0xb
	.long	.LASF31
	.byte	0x4
	.value	0x11d
	.byte	0xf
	.long	0x29a
	.byte	0x88
	.uleb128 0xb
	.long	.LASF32
	.byte	0x4
	.value	0x126
	.byte	0xf
	.long	0x82
	.byte	0x90
	.uleb128 0xb
	.long	.LASF33
	.byte	0x4
	.value	0x12f
	.byte	0x9
	.long	0x8e
	.byte	0x98
	.uleb128 0xb
	.long	.LASF34
	.byte	0x4
	.value	0x130
	.byte	0x9
	.long	0x8e
	.byte	0xa0
	.uleb128 0xb
	.long	.LASF35
	.byte	0x4
	.value	0x131
	.byte	0x9
	.long	0x8e
	.byte	0xa8
	.uleb128 0xb
	.long	.LASF36
	.byte	0x4
	.value	0x132
	.byte	0x9
	.long	0x8e
	.byte	0xb0
	.uleb128 0xb
	.long	.LASF37
	.byte	0x4
	.value	0x133
	.byte	0xa
	.long	0x2d
	.byte	0xb8
	.uleb128 0xb
	.long	.LASF38
	.byte	0x4
	.value	0x135
	.byte	0x7
	.long	0x63
	.byte	0xc0
	.uleb128 0xb
	.long	.LASF39
	.byte	0x4
	.value	0x137
	.byte	0x8
	.long	0x2a0
	.byte	0xc4
	.byte	0
	.uleb128 0xc
	.long	.LASF90
	.byte	0x4
	.byte	0x9b
	.byte	0xe
	.uleb128 0x9
	.long	.LASF41
	.byte	0x18
	.byte	0x4
	.byte	0xa1
	.byte	0x8
	.long	0x27e
	.uleb128 0xa
	.long	.LASF42
	.byte	0x4
	.byte	0xa2
	.byte	0x16
	.long	0x27e
	.byte	0
	.uleb128 0xa
	.long	.LASF43
	.byte	0x4
	.byte	0xa3
	.byte	0x14
	.long	0x284
	.byte	0x8
	.uleb128 0xa
	.long	.LASF44
	.byte	0x4
	.byte	0xa7
	.byte	0x7
	.long	0x63
	.byte	0x10
	.byte	0
	.uleb128 0x7
	.byte	0x8
	.long	0x249
	.uleb128 0x7
	.byte	0x8
	.long	0xa2
	.uleb128 0xd
	.long	0x96
	.long	0x29a
	.uleb128 0xe
	.long	0x39
	.byte	0
	.byte	0
	.uleb128 0x7
	.byte	0x8
	.long	0x241
	.uleb128 0xd
	.long	0x96
	.long	0x2b0
	.uleb128 0xe
	.long	0x39
	.byte	0x13
	.byte	0
	.uleb128 0xf
	.long	.LASF91
	.uleb128 0x10
	.long	.LASF45
	.byte	0x4
	.value	0x141
	.byte	0x1d
	.long	0x2b0
	.uleb128 0x10
	.long	.LASF46
	.byte	0x4
	.value	0x142
	.byte	0x1d
	.long	0x2b0
	.uleb128 0x10
	.long	.LASF47
	.byte	0x4
	.value	0x143
	.byte	0x1d
	.long	0x2b0
	.uleb128 0x7
	.byte	0x8
	.long	0x9d
	.uleb128 0x8
	.long	0x2dc
	.uleb128 0x11
	.long	.LASF48
	.byte	0x5
	.byte	0xa8
	.byte	0x19
	.long	0x284
	.uleb128 0x11
	.long	.LASF49
	.byte	0x5
	.byte	0xa9
	.byte	0x19
	.long	0x284
	.uleb128 0x11
	.long	.LASF50
	.byte	0x5
	.byte	0xaa
	.byte	0x19
	.long	0x284
	.uleb128 0x11
	.long	.LASF51
	.byte	0x6
	.byte	0x1a
	.byte	0xc
	.long	0x63
	.uleb128 0xd
	.long	0x2e2
	.long	0x322
	.uleb128 0x12
	.byte	0
	.uleb128 0x8
	.long	0x317
	.uleb128 0x11
	.long	.LASF52
	.byte	0x6
	.byte	0x1b
	.byte	0x1a
	.long	0x322
	.uleb128 0xd
	.long	0x90
	.long	0x343
	.uleb128 0xe
	.long	0x39
	.byte	0x1
	.byte	0
	.uleb128 0x10
	.long	.LASF53
	.byte	0x7
	.value	0x11a
	.byte	0xe
	.long	0x333
	.uleb128 0x10
	.long	.LASF54
	.byte	0x7
	.value	0x11b
	.byte	0xc
	.long	0x63
	.uleb128 0x10
	.long	.LASF55
	.byte	0x7
	.value	0x11c
	.byte	0x11
	.long	0x6f
	.uleb128 0x10
	.long	.LASF56
	.byte	0x7
	.value	0x121
	.byte	0xe
	.long	0x333
	.uleb128 0x10
	.long	.LASF57
	.byte	0x7
	.value	0x129
	.byte	0xc
	.long	0x63
	.uleb128 0x10
	.long	.LASF58
	.byte	0x7
	.value	0x12a
	.byte	0x11
	.long	0x6f
	.uleb128 0x2
	.long	.LASF59
	.byte	0x8
	.byte	0x3c
	.byte	0x1b
	.long	0x39
	.uleb128 0x9
	.long	.LASF60
	.byte	0x10
	.byte	0x8
	.byte	0x4b
	.byte	0x10
	.long	0x3c5
	.uleb128 0xa
	.long	.LASF61
	.byte	0x8
	.byte	0x4d
	.byte	0x23
	.long	0x3c5
	.byte	0
	.uleb128 0xa
	.long	.LASF62
	.byte	0x8
	.byte	0x4e
	.byte	0x23
	.long	0x3c5
	.byte	0x8
	.byte	0
	.uleb128 0x7
	.byte	0x8
	.long	0x39d
	.uleb128 0x2
	.long	.LASF63
	.byte	0x8
	.byte	0x4f
	.byte	0x3
	.long	0x39d
	.uleb128 0x9
	.long	.LASF64
	.byte	0x28
	.byte	0x8
	.byte	0x5c
	.byte	0xa
	.long	0x44d
	.uleb128 0xa
	.long	.LASF65
	.byte	0x8
	.byte	0x5e
	.byte	0x9
	.long	0x63
	.byte	0
	.uleb128 0xa
	.long	.LASF66
	.byte	0x8
	.byte	0x5f
	.byte	0x12
	.long	0x4e
	.byte	0x4
	.uleb128 0xa
	.long	.LASF67
	.byte	0x8
	.byte	0x60
	.byte	0x9
	.long	0x63
	.byte	0x8
	.uleb128 0xa
	.long	.LASF68
	.byte	0x8
	.byte	0x62
	.byte	0x12
	.long	0x4e
	.byte	0xc
	.uleb128 0xa
	.long	.LASF69
	.byte	0x8
	.byte	0x66
	.byte	0x9
	.long	0x63
	.byte	0x10
	.uleb128 0xa
	.long	.LASF70
	.byte	0x8
	.byte	0x68
	.byte	0xb
	.long	0x5c
	.byte	0x14
	.uleb128 0xa
	.long	.LASF71
	.byte	0x8
	.byte	0x69
	.byte	0xb
	.long	0x5c
	.byte	0x16
	.uleb128 0xa
	.long	.LASF72
	.byte	0x8
	.byte	0x6a
	.byte	0x16
	.long	0x3cb
	.byte	0x18
	.byte	0
	.uleb128 0x13
	.byte	0x28
	.byte	0x8
	.byte	0x5a
	.byte	0x9
	.long	0x47b
	.uleb128 0x14
	.long	.LASF73
	.byte	0x8
	.byte	0x7d
	.byte	0x5
	.long	0x3d7
	.uleb128 0x14
	.long	.LASF74
	.byte	0x8
	.byte	0x7e
	.byte	0x8
	.long	0x47b
	.uleb128 0x14
	.long	.LASF75
	.byte	0x8
	.byte	0x7f
	.byte	0xc
	.long	0x6f
	.byte	0
	.uleb128 0xd
	.long	0x96
	.long	0x48b
	.uleb128 0xe
	.long	0x39
	.byte	0x27
	.byte	0
	.uleb128 0x2
	.long	.LASF76
	.byte	0x8
	.byte	0x80
	.byte	0x3
	.long	0x44d
	.uleb128 0x3
	.byte	0x8
	.byte	0x7
	.long	.LASF77
	.uleb128 0x3
	.byte	0x8
	.byte	0x5
	.long	.LASF78
	.uleb128 0x15
	.long	.LASF79
	.byte	0x1
	.byte	0x4
	.byte	0xe
	.long	0x6a
	.uleb128 0x9
	.byte	0x3
	.quad	turn
	.uleb128 0x16
	.string	"m"
	.byte	0x1
	.byte	0x5
	.byte	0x11
	.long	0x48b
	.uleb128 0x9
	.byte	0x3
	.quad	m
	.uleb128 0x17
	.long	.LASF82
	.byte	0x1
	.byte	0x23
	.byte	0x5
	.long	0x63
	.quad	.LFB16
	.quad	.LFE16-.LFB16
	.uleb128 0x1
	.byte	0x9c
	.long	0x601
	.uleb128 0x18
	.long	.LASF80
	.byte	0x1
	.byte	0x23
	.byte	0xe
	.long	0x63
	.long	.LLST4
	.long	.LVUS4
	.uleb128 0x18
	.long	.LASF81
	.byte	0x1
	.byte	0x23
	.byte	0x1b
	.long	0x601
	.long	.LLST5
	.long	.LVUS5
	.uleb128 0x19
	.string	"ta"
	.byte	0x1
	.byte	0x24
	.byte	0xf
	.long	0x391
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.uleb128 0x19
	.string	"tb"
	.byte	0x1
	.byte	0x24
	.byte	0x13
	.long	0x391
	.uleb128 0x2
	.byte	0x91
	.sleb128 -32
	.uleb128 0x1a
	.quad	.LVL15
	.long	0x6c9
	.long	0x559
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x55
	.uleb128 0x9
	.byte	0x3
	.quad	m
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x54
	.uleb128 0x1
	.byte	0x30
	.byte	0
	.uleb128 0x1a
	.quad	.LVL16
	.long	0x6d6
	.long	0x588
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x55
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x54
	.uleb128 0x1
	.byte	0x30
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x51
	.uleb128 0x9
	.byte	0x3
	.quad	fa
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x52
	.uleb128 0x1
	.byte	0x30
	.byte	0
	.uleb128 0x1a
	.quad	.LVL17
	.long	0x6d6
	.long	0x5b7
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x55
	.uleb128 0x2
	.byte	0x77
	.sleb128 0
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x54
	.uleb128 0x1
	.byte	0x30
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x51
	.uleb128 0x9
	.byte	0x3
	.quad	fb
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x52
	.uleb128 0x1
	.byte	0x30
	.byte	0
	.uleb128 0x1a
	.quad	.LVL18
	.long	0x6e2
	.long	0x5ce
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x54
	.uleb128 0x1
	.byte	0x30
	.byte	0
	.uleb128 0x1a
	.quad	.LVL19
	.long	0x6e2
	.long	0x5e5
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x54
	.uleb128 0x1
	.byte	0x30
	.byte	0
	.uleb128 0x1c
	.quad	.LVL20
	.long	0x6ee
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x55
	.uleb128 0x9
	.byte	0x3
	.quad	m
	.byte	0
	.byte	0
	.uleb128 0x7
	.byte	0x8
	.long	0x90
	.uleb128 0x1d
	.string	"fb"
	.byte	0x1
	.byte	0x15
	.byte	0x7
	.long	0x8e
	.quad	.LFB15
	.quad	.LFE15-.LFB15
	.uleb128 0x1
	.byte	0x9c
	.long	0x668
	.uleb128 0x1e
	.string	"a"
	.byte	0x1
	.byte	0x15
	.byte	0x10
	.long	0x8e
	.long	.LLST2
	.long	.LVUS2
	.uleb128 0x1f
	.string	"i"
	.byte	0x1
	.byte	0x16
	.byte	0x9
	.long	0x63
	.long	.LLST3
	.long	.LVUS3
	.uleb128 0x1c
	.quad	.LVL9
	.long	0x6fb
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x55
	.uleb128 0x9
	.byte	0x3
	.quad	.LC1
	.byte	0
	.byte	0
	.uleb128 0x1d
	.string	"fa"
	.byte	0x1
	.byte	0x7
	.byte	0x7
	.long	0x8e
	.quad	.LFB14
	.quad	.LFE14-.LFB14
	.uleb128 0x1
	.byte	0x9c
	.long	0x6c9
	.uleb128 0x1e
	.string	"a"
	.byte	0x1
	.byte	0x7
	.byte	0x10
	.long	0x8e
	.long	.LLST0
	.long	.LVUS0
	.uleb128 0x1f
	.string	"i"
	.byte	0x1
	.byte	0x8
	.byte	0x9
	.long	0x63
	.long	.LLST1
	.long	.LVUS1
	.uleb128 0x1c
	.quad	.LVL3
	.long	0x6fb
	.uleb128 0x1b
	.uleb128 0x1
	.byte	0x55
	.uleb128 0x9
	.byte	0x3
	.quad	.LC0
	.byte	0
	.byte	0
	.uleb128 0x20
	.long	.LASF83
	.long	.LASF83
	.byte	0x9
	.value	0x2e7
	.byte	0xc
	.uleb128 0x21
	.long	.LASF84
	.long	.LASF84
	.byte	0x9
	.byte	0xeb
	.byte	0xc
	.uleb128 0x21
	.long	.LASF85
	.long	.LASF85
	.byte	0x9
	.byte	0xfc
	.byte	0xc
	.uleb128 0x20
	.long	.LASF86
	.long	.LASF86
	.byte	0x9
	.value	0x2ec
	.byte	0xc
	.uleb128 0x22
	.long	.LASF92
	.long	.LASF93
	.byte	0xa
	.byte	0
	.byte	0
	.section	.debug_abbrev,"",@progbits
.Ldebug_abbrev0:
	.uleb128 0x1
	.uleb128 0x11
	.byte	0x1
	.uleb128 0x25
	.uleb128 0xe
	.uleb128 0x13
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x1b
	.uleb128 0xe
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x10
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0x2
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x3
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0xe
	.byte	0
	.byte	0
	.uleb128 0x4
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0x8
	.byte	0
	.byte	0
	.uleb128 0x5
	.uleb128 0x35
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x6
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x7
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x8
	.uleb128 0x26
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x9
	.uleb128 0x13
	.byte	0x1
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xa
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x38
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xb
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x38
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xc
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xd
	.uleb128 0x1
	.byte	0x1
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xe
	.uleb128 0x21
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2f
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xf
	.uleb128 0x13
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x10
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x11
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x12
	.uleb128 0x21
	.byte	0
	.byte	0
	.byte	0
	.uleb128 0x13
	.uleb128 0x17
	.byte	0x1
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x14
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x15
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x16
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x17
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2117
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x18
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x17
	.uleb128 0x2137
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0x19
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x1a
	.uleb128 0x4109
	.byte	0x1
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x31
	.uleb128 0x13
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1b
	.uleb128 0x410a
	.byte	0
	.uleb128 0x2
	.uleb128 0x18
	.uleb128 0x2111
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x1c
	.uleb128 0x4109
	.byte	0x1
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x31
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1d
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x2117
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1e
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x17
	.uleb128 0x2137
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0x1f
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x17
	.uleb128 0x2137
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0x20
	.uleb128 0x2e
	.byte	0
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x6e
	.uleb128 0xe
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x21
	.uleb128 0x2e
	.byte	0
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x6e
	.uleb128 0xe
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x22
	.uleb128 0x2e
	.byte	0
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x6e
	.uleb128 0xe
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.byte	0
	.byte	0
	.byte	0
	.section	.debug_loc,"",@progbits
.Ldebug_loc0:
.LVUS4:
	.uleb128 0
	.uleb128 .LVU39
	.uleb128 .LVU39
	.uleb128 0
.LLST4:
	.quad	.LVL12-.Ltext0
	.quad	.LVL14-.Ltext0
	.value	0x1
	.byte	0x55
	.quad	.LVL14-.Ltext0
	.quad	.LFE16-.Ltext0
	.value	0x4
	.byte	0xf3
	.uleb128 0x1
	.byte	0x55
	.byte	0x9f
	.quad	0
	.quad	0
.LVUS5:
	.uleb128 0
	.uleb128 .LVU38
	.uleb128 .LVU38
	.uleb128 0
.LLST5:
	.quad	.LVL12-.Ltext0
	.quad	.LVL13-.Ltext0
	.value	0x1
	.byte	0x54
	.quad	.LVL13-.Ltext0
	.quad	.LFE16-.Ltext0
	.value	0x4
	.byte	0xf3
	.uleb128 0x1
	.byte	0x54
	.byte	0x9f
	.quad	0
	.quad	0
.LVUS2:
	.uleb128 0
	.uleb128 .LVU22
	.uleb128 .LVU22
	.uleb128 0
.LLST2:
	.quad	.LVL6-.Ltext0
	.quad	.LVL8-.Ltext0
	.value	0x1
	.byte	0x55
	.quad	.LVL8-.Ltext0
	.quad	.LFE15-.Ltext0
	.value	0x4
	.byte	0xf3
	.uleb128 0x1
	.byte	0x55
	.byte	0x9f
	.quad	0
	.quad	0
.LVUS3:
	.uleb128 .LVU20
	.uleb128 .LVU22
	.uleb128 .LVU22
	.uleb128 .LVU33
.LLST3:
	.quad	.LVL7-.Ltext0
	.quad	.LVL8-.Ltext0
	.value	0x2
	.byte	0x30
	.byte	0x9f
	.quad	.LVL8-.Ltext0
	.quad	.LVL11-.Ltext0
	.value	0x1
	.byte	0x53
	.quad	0
	.quad	0
.LVUS0:
	.uleb128 0
	.uleb128 .LVU5
	.uleb128 .LVU5
	.uleb128 0
.LLST0:
	.quad	.LVL0-.Ltext0
	.quad	.LVL2-.Ltext0
	.value	0x1
	.byte	0x55
	.quad	.LVL2-.Ltext0
	.quad	.LFE14-.Ltext0
	.value	0x4
	.byte	0xf3
	.uleb128 0x1
	.byte	0x55
	.byte	0x9f
	.quad	0
	.quad	0
.LVUS1:
	.uleb128 .LVU3
	.uleb128 .LVU5
	.uleb128 .LVU5
	.uleb128 .LVU16
.LLST1:
	.quad	.LVL1-.Ltext0
	.quad	.LVL2-.Ltext0
	.value	0x2
	.byte	0x30
	.byte	0x9f
	.quad	.LVL2-.Ltext0
	.quad	.LVL5-.Ltext0
	.value	0x1
	.byte	0x53
	.quad	0
	.quad	0
	.section	.debug_aranges,"",@progbits
	.long	0x2c
	.value	0x2
	.long	.Ldebug_info0
	.byte	0x8
	.byte	0
	.value	0
	.value	0
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.quad	0
	.quad	0
	.section	.debug_line,"",@progbits
.Ldebug_line0:
	.section	.debug_str,"MS",@progbits,1
.LASF8:
	.string	"__off_t"
.LASF12:
	.string	"_IO_read_ptr"
.LASF24:
	.string	"_chain"
.LASF7:
	.string	"size_t"
.LASF61:
	.string	"__prev"
.LASF75:
	.string	"__align"
.LASF30:
	.string	"_shortbuf"
.LASF74:
	.string	"__size"
.LASF79:
	.string	"turn"
.LASF47:
	.string	"_IO_2_1_stderr_"
.LASF18:
	.string	"_IO_buf_base"
.LASF88:
	.string	"turns.c"
.LASF77:
	.string	"long long unsigned int"
.LASF62:
	.string	"__next"
.LASF55:
	.string	"__timezone"
.LASF78:
	.string	"long long int"
.LASF4:
	.string	"signed char"
.LASF85:
	.string	"pthread_join"
.LASF25:
	.string	"_fileno"
.LASF13:
	.string	"_IO_read_end"
.LASF6:
	.string	"long int"
.LASF11:
	.string	"_flags"
.LASF19:
	.string	"_IO_buf_end"
.LASF28:
	.string	"_cur_column"
.LASF27:
	.string	"_old_offset"
.LASF32:
	.string	"_offset"
.LASF63:
	.string	"__pthread_list_t"
.LASF64:
	.string	"__pthread_mutex_s"
.LASF58:
	.string	"timezone"
.LASF41:
	.string	"_IO_marker"
.LASF48:
	.string	"stdin"
.LASF3:
	.string	"unsigned int"
.LASF0:
	.string	"long unsigned int"
.LASF91:
	.string	"_IO_FILE_plus"
.LASF69:
	.string	"__kind"
.LASF16:
	.string	"_IO_write_ptr"
.LASF73:
	.string	"__data"
.LASF51:
	.string	"sys_nerr"
.LASF43:
	.string	"_sbuf"
.LASF71:
	.string	"__elision"
.LASF2:
	.string	"short unsigned int"
.LASF20:
	.string	"_IO_save_base"
.LASF31:
	.string	"_lock"
.LASF26:
	.string	"_flags2"
.LASF38:
	.string	"_mode"
.LASF93:
	.string	"__builtin_puts"
.LASF49:
	.string	"stdout"
.LASF67:
	.string	"__owner"
.LASF45:
	.string	"_IO_2_1_stdin_"
.LASF92:
	.string	"puts"
.LASF89:
	.string	"/home/examiner/rares/2024/os/en/lecture-09"
.LASF17:
	.string	"_IO_write_end"
.LASF83:
	.string	"pthread_mutex_init"
.LASF90:
	.string	"_IO_lock_t"
.LASF40:
	.string	"_IO_FILE"
.LASF54:
	.string	"__daylight"
.LASF60:
	.string	"__pthread_internal_list"
.LASF44:
	.string	"_pos"
.LASF52:
	.string	"sys_errlist"
.LASF23:
	.string	"_markers"
.LASF59:
	.string	"pthread_t"
.LASF1:
	.string	"unsigned char"
.LASF5:
	.string	"short int"
.LASF29:
	.string	"_vtable_offset"
.LASF56:
	.string	"tzname"
.LASF46:
	.string	"_IO_2_1_stdout_"
.LASF66:
	.string	"__count"
.LASF65:
	.string	"__lock"
.LASF57:
	.string	"daylight"
.LASF10:
	.string	"char"
.LASF42:
	.string	"_next"
.LASF9:
	.string	"__off64_t"
.LASF14:
	.string	"_IO_read_base"
.LASF22:
	.string	"_IO_save_end"
.LASF76:
	.string	"pthread_mutex_t"
.LASF33:
	.string	"__pad1"
.LASF34:
	.string	"__pad2"
.LASF35:
	.string	"__pad3"
.LASF36:
	.string	"__pad4"
.LASF37:
	.string	"__pad5"
.LASF39:
	.string	"_unused2"
.LASF50:
	.string	"stderr"
.LASF81:
	.string	"argv"
.LASF84:
	.string	"pthread_create"
.LASF68:
	.string	"__nusers"
.LASF21:
	.string	"_IO_backup_base"
.LASF70:
	.string	"__spins"
.LASF80:
	.string	"argc"
.LASF72:
	.string	"__list"
.LASF86:
	.string	"pthread_mutex_destroy"
.LASF53:
	.string	"__tzname"
.LASF82:
	.string	"main"
.LASF15:
	.string	"_IO_write_base"
.LASF87:
	.string	"GNU C17 9.3.1 20200408 (Red Hat 9.3.1-2) -mtune=generic -march=x86-64 -g -O"
	.ident	"GCC: (GNU) 9.3.1 20200408 (Red Hat 9.3.1-2)"
	.section	.note.GNU-stack,"",@progbits
